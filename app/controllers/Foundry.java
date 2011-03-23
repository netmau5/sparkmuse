package controllers;

import controllers.SparkmuseController;
import controllers.Authorization;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.discussion.FoundryFacade;
import net.sparkmuse.discussion.WishSearchResponse;
import net.sparkmuse.discussion.WishResponse;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.common.CommitmentType;
import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.ajax.FragmentAjaxResponse;
import net.sparkmuse.user.UserVotes;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import play.mvc.Router;
import play.mvc.With;
import play.data.validation.Valid;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.templates.Template;
import play.templates.TemplateLoader;
import filters.AuthorizationFilter;

/**
 * @author neteller
 * @created: Mar 10, 2011
 */
@With(AuthorizationFilter.class) //@todo need to change for open access
public class Foundry extends SparkmuseController {

  @Inject static FoundryFacade foundryFacade;
  @Inject static Cache cache;

  public static void index(int page) {
    UserVO user = Authorization.getUserFromSession();
    WishSearchResponse wishSearchResponse = foundryFacade.findRecentWishes(user, PageChangeRequest.newInstance(page, cache, user, Wish.class, "Wish"));
    render(wishSearchResponse);
  }

  public static void tagged(String tag, int page) {
    UserVO user = Authorization.getUserFromSession();
    WishSearchResponse wishSearchResponse = foundryFacade.findTaggedWishes(tag, user, PageChangeRequest.newInstance(page, cache, user, Wish.class, "TaggedWish"));
    boolean isTagPage = true;
    renderTemplate("foundry/index.html", wishSearchResponse, tag, isTagPage);
  }

  public static void view(Long wishId) {
    WishResponse wishResponse = foundryFacade.findWishContent(wishId, Authorization.getUserFromSession());
    render(wishResponse);
  }

  public static void create() {
    boolean isEditMode = false;
    render(isEditMode);
  }

  public static void howItWorks() {
    render();
  }

  public static void submit(@Valid Wish wish, String userName) {
    final UserVO currentUser = Authorization.getUserFromSessionOrAuthenticate(true);

    Wish existingWish = null != wish.getId() ? foundryFacade.findWishBy(wish.getId()) : null;
    if (currentUser.isAdmin() && StringUtils.isNotBlank(userName)) {
      final UserProfile profile = userFacade.getUserProfile(userName);
      if (null == profile) {
        renderJSON(ValidationErrorAjaxResponse.only("userName", "User does not exist."));
      }
      wish.setAuthor(profile.getUser());
    }
    else if (null != wish.getId()) {
      if (!existingWish.getAuthor().getId().equals(currentUser.getId())) {
        renderJSON(ValidationErrorAjaxResponse.only("wish", "You may only edit your own Wish."));
      }
    }
    else {
      wish.setAuthor(currentUser);
    }

    final Wish savedSpark = foundryFacade.store(Reflections.overlay(
        existingWish,
        wish,
        "title",
        "description",
        "tags",
        "user",
        "authorUserId"
    ));
    final Map<String, Object> parameters = Maps.newHashMap();
    parameters.put("wishId", savedSpark.getId());
    renderJSON(new RedirectAjaxResponse(Router.reverse("Foundry.view", parameters).url));
  }

  public static void commit(@Required Long wishId, @Required CommitmentType commitmentType) {
    foundryFacade.commit(foundryFacade.findWishBy(wishId), commitmentType, Authorization.getUserFromSessionOrAuthenticate(true));
    renderJSON(new AjaxResponse());
  }

  public static void reply(@Valid Comment comment) {
    final UserVO author = Authorization.getUserFromSessionOrAuthenticate(true);
    comment.setAuthor(author);
    comment = foundryFacade.createComment(comment);

    final Template template = TemplateLoader.load("tags/comment.html");
    final Map<String, Object> args = Maps.newHashMap();
    args.put("_comment", comment);
    args.put("_userVotes", UserVotes.only(comment, author));
    final String content = template.render(args);

    renderJSON(new FragmentAjaxResponse(content));
  }

}
