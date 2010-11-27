var SM = {}; //global namespace
SM.Events = {
  TagRemoved: "TagRemoved",
  Submit: "Submit"
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
 *            a status code, a request parameters map, and a response object as json if given
 */
$(document).ready(function() {
  
});

$(document).ready(function() {
  //tooltips
  $("[title]").qtip({
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
});