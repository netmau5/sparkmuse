package controllers;

import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.Expertise;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.ajax.FragmentAjaxResponse;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.activity.ActivityService;
import net.sparkmuse.activity.ActivityStream;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.templates.Template;
import play.templates.TemplateLoader;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.data.validation.Required;
import play.mvc.Router;
import play.mvc.With;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import filters.AuthorizationFilter;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 24, 2010
 */
@With(AuthorizationFilter.class)
public class User extends SparkmuseController {

  @Inject static UserFacade userFacade;
  @Inject static ActivityService activityService;

  public static void view(String userName) {
    final UserProfile profile = userFacade.getUserProfile(userName);
    ActivityStream profileActivities = activityService.getProfileActivity(profile.getUser());
    render(profile, profileActivities);
  }

  public static void edit(String userName) {
    if (!userName.equalsIgnoreCase(Authorization.getUserFromSession().getUserName())) {
      User.view(userName);
    }
    
    final UserProfile profile = userFacade.getUserProfile(userName);
    final List<Expertise> expertises = Arrays.asList(Expertise.values());
    render(profile, expertises);
  }

  public static void saveProfile(@Valid UserProfile profile) {
    if (Validation.hasErrors()) {
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }

    final UserProfile existingProfile = userFacade.getUserProfile(Authorization.getUserFromSession().getUserName());
    userFacade.updateProfile(Reflections.overlay(
        existingProfile,
        profile,
        "name",
        "location",
        "email",
        "website",
        "showEmail",
        "expertises",
        "seekingConnections",
        "seekingExpertises",
        "bio",
        "displayBio"
    ));

    Map<String, Object> args = new HashMap();
    args.put("userName", existingProfile.getUser().getUserName());
    renderJSON(new RedirectAjaxResponse(Router.reverse("User.view", args).url));
  }

  public static void vote(String entity, Long id) {
    Logger.debug("Voting for [" + entity + "|" + id + "]");
    userFacade.recordUpVote(entity, id, Authorization.getUserFromSessionOrAuthenticate(true));
    renderJSON(new AjaxResponse());
  }

  public static void inviteFriend(@Required String friend) {
    final int remainingInvites = userFacade.inviteFriend(Authorization.getUserFromSessionOrAuthenticate(true), friend);

    final Template template = TemplateLoader.load("User/tweet-after-invite-dialog.html");
    final Map<String, Object> args = Maps.newHashMap();
    args.put("name", friend.startsWith("@") ? friend : "@" + friend);
    args.put("remainingInvites", remainingInvites);
    final String content = template.render(args);

    renderJSON(new FragmentAjaxResponse(content));
  }

  public static void tweet(String message) {
    if (StringUtils.isNotBlank(message)) {
      userFacade.tweet(Authorization.getUserFromSessionOrAuthenticate(true), message);
    }
    renderJSON(new AjaxResponse());
  }

  public static void clearNotification(Long notificationId) {
    userFacade.clearNotification(Authorization.getUserFromSessionOrAuthenticate(true), notificationId);
    renderJSON(new AjaxResponse());
  }

}
