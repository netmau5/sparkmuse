$(document).ready(function() {

  if (!SM.Discuss) { SM.Discuss = {}; }

  SM.Discuss.Controller = Backbone.Controller.extend({

    pageModel: null,
    currentGroup: null,

    //discussion count tracking
    beforeFetchCount: null,
    newCount: 0,

    routes: {
      ":groupName": "changeGroup"
    },

    initialize: function(options) {
      this.pageModel = options.pageModel;
      this.currentGroup = options.currentGroup;
      var self = this;

      this.pageModel.bind("change:group", function(){
        self.currentGroup = self.pageModel.group.get("name");
        clearInterval(self.interval);
        self.addRefreshTimer();
      });

      //updates
      this.addRefreshTimer();
    },

    addRefreshTimer: function() {
      var self = this;
      this.interval = setInterval(function(){
        self.beforeFetchCount = self.pageModel.discussions.length;
        SM.LoadingIndicator.show();
        self.pageModel.fetch({
          groupName: self.currentGroup || "General",
          success: function(pageModel, response) {
            SM.LoadingIndicator.hide();
            var delta = pageModel.discussions.length - self.beforeFetchCount;
            if (delta > 0) {
              self.newCount += delta;
              document.title = "(" + self.newCount + ") Sparkmuse Discussions";
            }
          },
          error: SM.LoadingIndicator.hide
        });
      }, 120000);
    },

    changeGroup: function(groupName) {
      groupName = groupName || "General";
      if (groupName !== this.currentGroup) {
        SM.LoadingIndicator.show();
        document.title = "Sparkmuse Discussions";
        this.pageModel.fetch({
          groupName: groupName,
          success: SM.LoadingIndicator.hide,
          error: SM.LoadingIndicator.hide
        });
        this.currentGroup = groupName;
      }
    }

  });

  SM.Discuss.DiscussionPageModel = Backbone.Model.extend({

    initialize: function() {
      var group = this.group = new SM.Discuss.GroupModel(this.get("group")),
          discussions = this.discussions = new SM.Discuss.Discussions(this.get("discussions"))
          self = this;

      this.bind("change:group", function(){
        group.set(self.get("group"));
      });

      this.bind("change:discussions", function(){
        discussions.refresh(self.get("discussions"));
      });

    },

    fetch: function(options) {
      if (options.groupName) {
        this.url = "/discuss/" + options.groupName;
      }
      else {
        this.url = "/discuss";
      }

      Backbone.Model.prototype.fetch.call(this, options);
    }

  });

  SM.Discuss.GroupModel = Backbone.Model.extend({

  });

  SM.Discuss.DiscussionModel = Backbone.Model.extend({

  });

  SM.Discuss.Discussions = Backbone.Collection.extend({

    model: SM.Discuss.DiscussionModel,

    refresh: function(refreshDiscussionModels) {
      var existingIds = this.pluck("id"),
          refreshedIds = _.pluck(refreshDiscussionModels, "id"),
          refreshedIdsCopy = refreshedIds.slice(0),
          existingIdsCopy = existingIds.slice(0);

      refreshedIdsCopy.splice(0, 0, existingIds);
      existingIdsCopy.splice(0, 0, refreshedIds);

      var toRemove = _.without.apply(this, refreshedIdsCopy), // [array], arg,arg,arg - refreshed ids without existing
          toAdd = _.without.apply(this, existingIdsCopy), // [array], arg,arg,arg - existing ids without refreshed
          self = this;

      _.each(toRemove, function(i){
        self.remove(self.get(i));
      });

      _.each(toAdd, function(i){
        self.add(refreshDiscussionModels[refreshedIds.indexOf(i)]);
      });

      this.trigger("refresh");
    },

    comparator: function(discussion) {
      return -discussion.get("created");
    }

  });

  SM.Discuss.PageView = Backbone.View.extend({

    discussionViews: {},

    initialize: function() {
      var breadcrumbsView = new SM.Discuss.BreadcrumbsView({
        model: this.model.group
      });
      $("#breadcrumbs-container").append(breadcrumbsView.render().el);

      var groupInfoView = new SM.Discuss.GroupInfoView({
        model: this.model.group,
        el: $("#groupinfo-other")
      });
      $(".discussion-sidebar-wrapper").prepend(groupInfoView.render().el);

      _.bindAll(this, "addDiscussion", "removeDiscussion");
      this.model.discussions.each(this.addDiscussion);
      this.model.discussions.bind("add", this.addDiscussion);
      this.model.discussions.bind("remove", this.removeDiscussion);
    },

    addDiscussion: function(newDiscussion) {
      var view = new SM.Discuss.DiscussionsView({
        model: newDiscussion
      }),
      self = this;

      this.discussionViews[newDiscussion.id] = view;

      //add to dom
      var insertBeforeDiscussion = this.model.discussions.detect(function(discussion){
        return newDiscussion.get("created") > discussion.get("created") &&
            self.discussionViews[discussion.id];
      });
      if (insertBeforeDiscussion) {
        $(view.render().el).insertBefore(this.discussionViews[insertBeforeDiscussion.id].el);
      }
      else {
        $("#discussions-container").append(view.render().el);
      }
    },

    removeDiscussion: function(discussion) {
      this.discussionViews[discussion.id].remove();
      delete this.discussionViews[discussion.id];
    }

  });

  SM.Discuss.DiscussionsView = Backbone.View.extend({
    
    tagName: "div",
    className: "discussion-wrapper",

    render: function() {
      $(this.el).html(this.model.get("html")).fadeIn();
      return this;
    },

    remove: function() {
      $(this.el).stop().animate(
      {
        height: 0,
        opacity: 0
      },
      {
        complete: function(){
          $(this).remove();
        }
      }
      );
    }

  });

  SM.Discuss.BreadcrumbsView = Backbone.View.extend({
    tagName: "div",
    className: "breadcrumbs",
    template: $("#template-breadcrumbs").text(),

    initialize: function() {
      var self = this;
      this.model.bind("change", function(){
        self.render();
      });
      $(this.el).hide();
    },

    render: function(){
      if (this.model.get("name") === "General") {
        $(this.el).slideUp();
      }
      else {
        $(this.el).html(Mustache.to_html(this.template, this.model.toJSON()))
            .slideDown();
      }
      return this;
    }
  });

  SM.Discuss.GroupInfoView = Backbone.View.extend({
    template: $("#template-groupinfo").text(),

    initialize: function() {
      var self = this;
      this.model.bind("change", function(){
        self.render();
      });
    },

    render: function() {
      if (this.model.get("name") === "General") {
        $("#groupinfo-general").slideDown();
        $(this.el).slideUp();
      }
      else {
        $("#groupinfo-general").slideUp();
        $(this.el).html(Mustache.to_html(this.template, this.model.toJSON()))
            .slideDown();
      }

      return this;
    }
  });

})