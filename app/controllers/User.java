package controllers;

import net.sparkmuse.user.UserFacade;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.Expertise;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.common.Reflections;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.mvc.Router;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 24, 2010
 */
public class User extends SparkmuseController {

  @Inject static UserFacade userFacade;

  public static void view(String userName) {
    final UserProfile profile = userFacade.getUserProfile(userName);
    boolean isView = true;
    render(profile, isView);
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
    userFacade.updateProfile(Reflections.overlay(existingProfile, profile));

    Map<String, Object> args = new HashMap();
    args.put("userName", existingProfile.getUser().getUserName());
    renderJSON(new RedirectAjaxResponse(Router.reverse("User.view", args).url));
  }

  public static void vote(String entity, Long id) {
    Logger.debug("Voting for [" + entity + "|" + id + "]");
    userFacade.recordUpVote(entity, id, Authorization.getUserFromSessionOrAuthenticate(true));
    renderJSON(new AjaxResponse());
  }

}
