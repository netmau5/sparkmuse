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
        var parms = {
          url: form.attr("target"),
          data: formParameterBuilder.call(this),
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
      alert("validation error");
      $.modal.close();
    },

    handleRedirectResponse: function(response) {
      window.target = response.targetUrl;
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
 * tags: replaces the class a.tag-link with button-style tags;
 * add class "editable" to add a remove control; removal will fire SM.Events.TagRemoved
 */
(function($){
  //@required - tagName
  //@optional - editable
  $.tag = function(props) {
    var tagTemplate = $("#tag-template").html(); //provided in main template via TagTemplate
    var newTag = $(Mustache.to_html(
        tagTemplate,
        $.extend({editable: false}, props)
    ));
    newTag.find("a.tag-control").click(function(){
      newTag.trigger(SM.Events.TagRemoved);
      var a = $(this);
      a.parent().remove();
      return false;
    });
    return newTag;
  };

  $.fn.tag = function(editable) {
    $(this).each(function(){
      var tagLink = $(this),
          newTag = $.tag({
            tagName: tagLink.html(),
            editable: editable
          });

      tagLink.parent().append(newTag);
      tagLink.remove();

      return newTag;
    });
  }
})(jQuery);

$(document).ready(function() {
  $("a.tag-link").each(function() {
    var tagLink = $(this);
    tagLink.tag(tagLink.hasClass("editable"));
  });
});