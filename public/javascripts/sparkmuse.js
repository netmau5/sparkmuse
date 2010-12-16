var SM = {}; //global namespace
SM.Events = {
  TagRemoved: "TagRemoved",
  Submit: "Submit"
};

SM.newId = (function(){
  var id = 1;
  return function(prefix) {
    return (prefix || "id") + "-" + id++;
  }
})();

//response handler
(function($){
  var FormHandler = {
    init: function(formEl, formParameterBuilder){
      var form = $(formEl),
          formParameterBuilder = formParameterBuilder || function() {
            return form.serialize();
          };

      form.submit(function(){
        form.trigger(SM.Events.Submit);
        var params = formParameterBuilder.call(this);
        //dont send params that are falsey, ie empty string
        for (var i in params) {
          if (params.hasOwnProperty(i) && !params[i]) { delete params[i]; }
        }
        var parms = {
          url: form.attr("target"),
          data: params,
          type: form.attr("method"),
          success: FormHandler.responseHandler,
          error: FormHandler.handleSystemErrorResponse,
          dataType: "json",
          context: form
        };
        $.ajax(parms);

        return false;
      });
    },

    responseHandler: function(response) {
      FormHandler.determineCorrectHandler(response).call(this, response);
    },

    determineCorrectHandler: function(response) {
      if (!response.type) throw "Unknown response format.";
      switch(response.type){
        case "SUCCESS": return FormHandler.handleSuccessResponse;
        case "VALIDATION_ERROR": return FormHandler.handleValidationErrorResponse;
        case "SYSTEM_ERROR": return FormHandler.handleSystemErrorResponse;
        case "REDIRECT": return FormHandler.handleRedirectResponse;
        default: throw "Unknown response type.";
      }
    },

    handleSystemErrorResponse: function() {
      $.modal.close();
      alert("error");
    },

    handleSuccessResponse: function(response) {
      $.modal.close();
    },

    handleValidationErrorResponse: function(response) {
      var validationErrors = response.validationErrors,
          errorContainers = $(".error-message");

      errorContainers.removeClass("error").html("");

      for (var parameterName in validationErrors) {
        if (validationErrors.hasOwnProperty(parameterName)) {
          var parameterValue = validationErrors[parameterName];
          errorContainers.filter(".error-message[parameter='" + parameterName + "']")
              .html(parameterValue.join("<br/>"))
              .addClass("error");
        }
      }

      $.modal.close();
    },

    handleRedirectResponse: function(response) {
      window.location = response.targetUrl;
      $.modal.close();
    }
  }

  $.fn.formHandler = function(url, formParameterBuilder) {
    FormHandler.init(this, url, formParameterBuilder);
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
      toReturn.a[toReturn.length] = val;
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
          toReturn.a.splice(i, 1); break;
        }
      }
      return toReturn;
    },
    clone: function(){
      return new Collection(this.a);
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
 * href:      #[url + query string]- a GET request is made to this address
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

  $("a[href^='#']").live("click", function(e){
    e.stopPropagation();
    var el = $(this);

    if (el.attr("href").length > 1) {
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
          url: el.attr("href").substring(1),
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
  //tooltips
  $("[title]").live("mouseenter", function() {
    var e = $(this);
    if(!e.data("qtip-init")) {
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


  //top button
  $(".top-arrow").click(function(){
    $('body').animate({scrollTop:0}, 'fast');
    return false;
  });

  //lightboxes
  $(document.body).append("<div style='display:none;' id='modal-lightbox'></div>");
  $(".lightbox").click(function(){
    $("#modal-lightbox").html("")
        .append($(this).clone().removeClass("lightbox"))
        .modal({
          close: true
        });
  });
});