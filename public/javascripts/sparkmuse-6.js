var SM = {}; //global namespace
SM.Events = {
  TagRemoved: "TagRemoved",
  Submit: "Submit",
  SubmitEnd: "SubmitEnd",
  SubmitEndError: "SubmitEndError",
  SubmitEndSuccess: "SubmitEndSuccess"
};

SM.newId = (function(){
  var id = 1;
  return function(prefix) {
    return (prefix || "id") + "-" + id++;
  }
})();

SM.disable = function(el) { $(el).attr("disabled", true); };
SM.isDisabled = function(el) { return $(el).attr("disbled"); };
SM.enable = function(el) { $(el).attr("disabled", false); };
SM.formSubmitModal = function(){
  //tag in modals-common.html (includesBottom)
  $("#modal-submission").modal({
    opacity: 50,
    close: false,
    onClose: undefined
  });
};
SM.formSubmitModalClose = function(){
  $.modal.close(); 
};

//loading indicator
SM.LoadingIndicator = {
  show: function() {
    var indicator = $("#loading-indicator");
    if (!$("#loading-indicator").size()) {
      indicator = $("<div id='loading-indicator'>Loading</div>");
      $(document.body).append(indicator);
    }
    indicator.slideDown();
  },
  hide: function() {
    $("#loading-indicator").slideUp();
  }
};

//response handler
(function($){
  var FormHandler = {
    init: function(formEl, formParameterBuilder){
      var form = $(formEl),
          formParameterBuilder = formParameterBuilder || function() {
            return form.serialize();
          };

      form.submit(function(){
        if (!SM.isDisabled(form)) {
          SM.disable(form);
          FormHandler.clearErrors();

          form.trigger(SM.Events.Submit);
          var i, params = formParameterBuilder.call(this),
              context = params._context ? $(params._context) : form,
              filteredParams = {};

          //dont send params that are falsey, ie empty string
          //dont send reserved property names (ie, those starting with _)
          for (i in params) {
            if (!(params.hasOwnProperty(i) && (!params[i] || i.indexOf("_") === 0))) {
              filteredParams[i] = params[i];
            }
          }

          var parms = {
            url: form.attr("action"),
            data: FormHandler.playcate(filteredParams),
            type: form.attr("method"),
            success: FormHandler.newSuccessResponseHandler(form, params),
            error: FormHandler.newFailureResponseHandler(form),
            dataType: "json",
            context: context
          };
          $.ajax(parms);

          return false;
        }
      });
    },

    //Map properties to Play-style request args
    playcate: function(params) {
      var k, r = {};
      for (k in params) {
        if (params.hasOwnProperty(k)) {
          $.extend(r, FormHandler.playcatify(k, params[k]));
        }
      }
      return r;
    },

    playcatify: function(k, v) {
      var r = {};
      if ($.isArray(v)) {
        var i;
        for (i = 0; i < v.length; i++) {
          $.extend(r, FormHandler.playcatify(k + "[" + i + "]", v[i]));
        }
        return r;
      }
      else if ($.isPlainObject(v)) {
        var kv;
        for (kv in v) {
          if (v.hasOwnProperty(kv)) {
            $.extend(r, FormHandler.playcatify(k + "." + kv, v[kv]));
          }
        }
        return r;
      }
      else {
        r[k] = v;
        return r;
      }
    },

    newSuccessResponseHandler: function(form, params) {
      return function(response) {
        SM.enable(form);
        var handler = params["_" + response.type.toLowerCase()] || FormHandler.determineCorrectHandler(response);
        form.trigger(SM.Events.SubmitEnd);
        form.trigger(FormHandler.determineCorrectEndEvent(response));
        handler.call(this, response);
      }
    },

    determineCorrectHandler: function(response) {
      if (!response.type) throw "Unknown response format.";
      switch(response.type){
        case "SUCCESS": return FormHandler.handleSuccessResponse;
        case "JSON": return FormHandler.handleSuccessResponse;
        case "VALIDATION_ERROR": return FormHandler.handleValidationErrorResponse;
        case "SYSTEM_ERROR": return FormHandler.newFailureResponseHandler;
        case "REDIRECT": return FormHandler.handleRedirectResponse;
        //a _success property should be defined to augment default success handler for fragments
        case "FRAGMENT": return FormHandler.handleFragmentResponse;
        case "INVALID_REQUEST_ERROR": return FormHandler.handleRequestErrorResponse;
        default: throw "Unknown response type.";
      }
    },

    determineCorrectEndEvent: function(response) {
      if (!response.type) throw "Unknown response format.";
      switch(response.type){
        case "SUCCESS": return SM.Events.SubmitEndSuccess;
        case "JSON": return SM.Events.SubmitEndSuccess;
        case "VALIDATION_ERROR": return SM.Events.SubmitEndError;
        case "SYSTEM_ERROR": return SM.Events.SubmitEndError;
        case "REDIRECT": return SM.Events.SubmitEndSuccess;
        case "FRAGMENT": return SM.Events.SubmitEndSuccess;
        case "INVALID_REQUEST_ERROR": return SM.Events.SubmitEndError;
        default: throw "Unknown response type.";
      }
    },

    newFailureResponseHandler: function(form) {
      return function() {
        SM.enable(form);
        alert("Oops! System error has occured and has been logged.  Please contact Sparkmuse if this problem persists.");
        //@todo change alert to a notification panel, scroll to top
        form.trigger(SM.Events.SubmitEndError);
      }
    },

    handleSuccessResponse: function(response) {
    },

    handleFragmentResponse: function(response) {
      $(document.body).prepend(response.fragment);
    },

    handleRequestErrorResponse: function(response) {
      //tag in modals-common.html (includesBottom)
      $("#modal-request-error .message").html(response.message);
      $("#modal-request-error").modal({
        opacity: 50,
        close: false,
        onClose: undefined
      })
    },

    handleValidationErrorResponse: function(response) {
      var validationErrors = response.validationErrors,
          errorContainers = $(".error-message");

      FormHandler.clearErrors();

      for (var parameterName in validationErrors) {
        if (validationErrors.hasOwnProperty(parameterName)) {
          var parameterValue = validationErrors[parameterName];
          errorContainers.filter(".error-message[parameter='" + parameterName + "']")
              .html(parameterValue.join("<br/>"))
              .addClass("error");
        }
      }
    },

    handleRedirectResponse: function(response) {
      window.location = response.targetUrl;
    },

    clearErrors: function() {
      $(".error-message").removeClass("error").html("");
    }
  }

  $.fn.formHandler = function(url, formParameterBuilder) {
    FormHandler.init(this, url, formParameterBuilder);
    return this;
  }
})(jQuery);

//collections
(function($){
  var Collection = function(args){
    if (!args) this.a = [];
    else if ($.isArray(args[0])) this.a = args[0];
    else if ($.isArray(args)) this.a = args;
    else this.a = [];

    this.length = this.a.length;
  }

  Collection.prototype = {
    toArray: function(){return this.a;},
    add: function(val){
      var toReturn = this.clone();
      toReturn.a[toReturn.length++] = val;
      return toReturn;
    },
    contains: function(val) {
      for (var i = this.a.length - 1; i >= 0; i--) {
        if (this.a[i] == val) return true;
      }
      return false;
    },
    remove: function(val) {
      var toReturn = this.clone();
      for (var i = this.a.length - 1; i >= 0; i--) {
        if (this.a[i] == val) {
          toReturn.a.splice(i, 1);
          toReturn.length--;
          break;
        }
      }
      return toReturn;
    },
    clone: function(){
      return new Collection(this.a);
    },
    each: function(func){
      var j = 0, i = this.length;
      while (--i >= j) {
        func.call(this, this.a[i]);
      }
    },
    map: function(func) {
      var toReturn = new Collection();
      this.each(function(x) {
        toReturn = toReturn.add(func.call(this, x));
      });
      return toReturn;
    },
    findAll: function(func){
      var toReturn = new Collection();
      this.each(function(x) {
        if (func.call(this, x)) { toReturn = toReturn.add(x); }
      });
      return toReturn;
    },
    join: function(separator) { return this.a.join(separator || ""); }
  }

  $["collection"] = function() {
    return new Collection(arguments);
  }
})(jQuery);

/**
 * Ajax links - scans anchor tags with an href value starting with "#" character and configures
 * them to invoke a RESTful request ansychronously.  Until the response is received, the button
 * will be issue additional requests.
 *
 * href:      #![url + query string]- a GET request is made to this address
 * callback:  JS function to be called upon completion, will be passed three parameters:
 *            a status code, a request query parameters map, and a response object as json if given
 */
$(document).ready(function() {
  var queryStringToMap = function(url) {
    var tokens = url.split("?"),
        toReturn = {};
    if (tokens.length == 2) {
      var pairs = tokens[1].split("&");
      for (var i in pairs) {
        var pair = pairs[i].split("=");
        toReturn[pair[0]] = pair[1];
      }
    }
    return toReturn;
  }

  SM.LikedCallback = function(status, request, response){
    if (status === 200) {
      var el = $(this);
      el.html(parseInt(el.html()) + 1);
      el.addClass("liked");
      el.attr("href", "#");
    }
  }

  $("a[href^='#!']").live("click", function(e){
    e.stopPropagation();
    var el = $(this);

    if (el.attr("href").length > 2) {
      if (!el.attr("active") || el.attr("active") === "false") {
        var request = queryStringToMap(el.attr("href"));
        var callback = function(response, desc, xmlRequest) {
          var callback = eval(el.attr("callback"));
          if (callback && callback instanceof Function) {
            callback.call(this, xmlRequest.status, request, response);
          }
          el.attr("active", "false");
        };

        el.attr("active", "true");
        var parms = {
          url: el.attr("href").substring(2),
          success: callback,
          error: callback,
          dataType: "json",
          context: this
        };
        $.ajax(parms);
      }
    }

    return false;
  });
});

$(document).ready(function() {
  //minheight
  var bodyHeight = $(document.body).height(),
      windowHeight = $(window).height();
  if (bodyHeight < windowHeight) {
    $("#body-container").css({minHeight: 450 + windowHeight - bodyHeight});
  }

  //tooltips
  $("[title]:not(iframe)").live("mouseenter", function() {
    var e = $(this);
    if(!e.data("qtip-init") && typeof this.title === "string") {
      e.qtip({
        style: {
          classes: "ui-tooltip-sparkmuse"
        },
        position: {
          my: "bottom center",
          at: "top center",
          target: 'mouse',
          adjust: { y: -15 }
        }
      });
      e.data("qtip-init", true);
      e.trigger("mouseenter");
    }
  });

  //lightboxes
  $(document.body).append("<div style='display:none;' id='modal-lightbox'></div>");
  $(".lightbox:not(iframe)").click(function(){
    $("#modal-lightbox").html("")
        .append($(this).clone().removeClass("lightbox"))
        .modal({
          close: true
        });
  });

  //preload modal images
  $.preload([
    "/public/images/textures/stars-black.gif",
    "/public/images/blades_small_animated.gif",
    "/public/images/textures/horizontal-tile-blue.jpg",
  ]);

  //form submit error
  $("form").live(SM.Events.SubmitEndError, SM.formSubmitModalClose);

});
