package controllers;

import controllers.SparkmuseController;
import controllers.Authorization;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.discussion.FoundryFacade;
import net.sparkmuse.discussion.WishSearchResponse;
import net.sparkmuse.discussion.WishResponse;
import net.sparkmuse.discussion.WishSearchRequest;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.common.CommitmentType;
import net.sparkmuse.common.AccessibleBy;
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
@With(AuthorizationFilter.class)
@AccessibleBy(AccessLevel.UNAUTHORIZED)
public class Foundry extends SparkmuseController {

  @Inject static FoundryFacade foundryFacade;
  @Inject static Cache cache;

  public static void index(WishSearchRequest.Filter filter, int page) {
    UserVO user = Authorization.getUserFromSession();
    WishSearchResponse wishSearchResponse = foundryFacade.search(WishSearchRequest.newSearch(
        user,
        filter,
        PageChangeRequest.newInstance(page, cache, session.getId(), Wish.class, "Wish")
    ));
    render(wishSearchResponse, filter);
  }

  public static void tagged(String tag, int page) {
    UserVO user = Authorization.getUserFromSession();
    WishSearchResponse wishSearchResponse = foundryFacade.search(WishSearchRequest.newTagSearch(
        user,
        tag,
        PageChangeRequest.newInstance(page, cache, session.getId(), Wish.class, "TaggedWish")
    ));
    boolean isTagPage = true;
    renderTemplate("Foundry/index.html", wishSearchResponse, tag, isTagPage);
  }

  public static void view(Long wishId) {
    WishResponse wishResponse = foundryFacade.findWishContent(wishId, Authorization.getUserFromSession());
    render(wishResponse);
  }

  public static void howItWorks() {
    render();
  }

  @AccessibleBy(AccessLevel.FOUNDRY)
  public static void create() {
    boolean isEditMode = false;
    render(isEditMode);
  }

  @AccessibleBy(AccessLevel.FOUNDRY)
  public static void edit(Long wishId) {
    boolean isEditMode = true;
    Wish wish = foundryFacade.findWishBy(wishId);
    renderTemplate("Foundry/create.html", isEditMode, wish);
  }

  @AccessibleBy(AccessLevel.FOUNDRY)
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

  @AccessibleBy(AccessLevel.FOUNDRY)
  public static void welcome() {
    render();
  }

  @AccessibleBy(AccessLevel.FOUNDRY)
  public static void commit(@Required Long wishId, @Required CommitmentType commitmentType) {
    foundryFacade.commit(foundryFacade.findWishBy(wishId), commitmentType, Authorization.getUserFromSessionOrAuthenticate(true));
    renderJSON(new AjaxResponse());
  }

  @AccessibleBy(AccessLevel.FOUNDRY)
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
