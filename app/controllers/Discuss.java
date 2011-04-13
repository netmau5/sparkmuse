package controllers;

import net.sparkmuse.data.entity.*;
import net.sparkmuse.discussion.*;
import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import net.sparkmuse.ajax.FragmentAjaxResponse;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.common.DiscussionType;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.client.discussions.DiscussionModel;
import net.sparkmuse.client.discussions.DiscussionPageModel;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.mvc.Router;
import play.mvc.With;
import play.Logger;
import play.templates.Template;
import play.templates.TemplateLoader;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

import com.google.common.collect.Maps;
import filters.AuthorizationFilter;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
@With(AuthorizationFilter.class)
public class Discuss extends SparkmuseController {

  @Inject static DiscussionFacade discussionFacade;

  public static void index(String groupName) {
    DiscussionsRequest discussionsRequest = new DiscussionsRequest(Authorization.getUserFromSession())
        .forGroup(groupName);
    DiscussionsResponse discussionsResponse = discussionFacade.getRecentDiscussions(discussionsRequest);
    DiscussionPageModel discussionPageModel = DiscussionPageModel.newInstance(discussionsResponse, (UserProfile) renderArgs.get("currentUserProfile"));
    DiscussionGroups discussionGroups = discussionFacade.getGroups();

    if (request.isAjax()) {
      renderJSON(discussionPageModel);
    }
    else {
      render(discussionsResponse, discussionPageModel, discussionGroups); //@todo dont render discussionsResponse
    }
  }

  public static void view(Long discussionId) {
    DiscussionResponse discussionResponse = discussionFacade.getDiscussionBy(discussionId, Authorization.getUserFromSession());
    render(discussionResponse);
  }

  public static void submit(@Valid Discussion discussion, String userName) {
    if (StringUtils.isEmpty(discussion.getUrl()) && StringUtils.isEmpty(discussion.getContent())) {
      Validation.addError("discussion.url", "Either URL or Content is required.");
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }
    else if (StringUtils.isNotEmpty(discussion.getUrl()) && StringUtils.isNotEmpty(discussion.getContent())) {
      Validation.addError("discussion.url", "Only one of URL or Content may be used.");
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }

    //populate discussiontype
    if (StringUtils.isNotBlank(discussion.getUrl())) {
      discussion.setDiscussionType(DiscussionType.LINK);
    }
    else if (StringUtils.isNotBlank(discussion.getContent())) {
      discussion.setDiscussionType(DiscussionType.DISCUSS);
    }
    else {
      Validation.addError("discussion", "Unknown discussion type.");
      Logger.error("Unknown discussion type for discussion [" + discussion + "]");
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }


    final UserVO currentUser = Authorization.getUserFromSessionOrAuthenticate(true);
    Discussion existingDiscussion = null != discussion.getId() ? discussionFacade.findDiscussionBy(discussion.getId()) : null;

    if (currentUser.isAdmin() && StringUtils.isNotBlank(userName)) {
      final UserProfile profile = userFacade.getUserProfile(userName);
      if (null == profile) {
        renderJSON(ValidationErrorAjaxResponse.only("userName", "User does not exist."));
      }
      discussion.setAuthor(profile.getUser());
    }
    else if (null != discussion.getId()) {
      if (!existingDiscussion.getAuthor().getId().equals(currentUser.getId())) {
        renderJSON(ValidationErrorAjaxResponse.only("spark", "You may only edit your own Spark."));
      }
    }
    else {
      discussion.setAuthor(currentUser);
    }

    final Discussion savedDiscussion = discussionFacade.storeDiscussion(Reflections.overlay(
        existingDiscussion,
        discussion,
        "title",
        "url",
        "content",
        "displayContent",
        "discussionType",
        "author",
        "authorUserId"
    ));

//    renderJSON(new FragmentAjaxResponse("/tags/discuss/discussionInfo.html", ImmutableMap.<String, Object>of(
//        "discussion", savedDiscussion,
//        "userVotes", UserVotes.only(savedDiscussion, savedDiscussion.getAuthor()),
//        "userProfile", userFacade.findUserProfileBy(currentUser.getId())
//    )));

    final Map<String, Object> parameters = Maps.newHashMap();
    parameters.put("discussionId", savedDiscussion.getId());
    renderJSON(new RedirectAjaxResponse(Router.reverse("Discuss.view", parameters).url));
  }

  public static void reply(@Valid DiscussionComment comment) {
    final UserVO author = Authorization.getUserFromSessionOrAuthenticate(true);
    comment.setAuthor(author);
    comment = discussionFacade.createComment(comment);

    final Template template = TemplateLoader.load("tags/comment.html");
    final Map<String, Object> args = Maps.newHashMap();
    args.put("_comment", comment);
    args.put("_userVotes", UserVotes.only(comment, author));
    final String content = template.render(args);

    renderJSON(new FragmentAjaxResponse(content));
  }

}
