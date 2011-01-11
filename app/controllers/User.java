package controllers;

import net.sparkmuse.user.UserFacade;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;

import java.util.Map;
import java.util.HashMap;

import javax.inject.Inject;

import play.Logger;
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
    render(profile);
  }

  public static void saveProfile(UserProfile profile) {
    profile.setUser(Authorization.getUserFromSession());
    userFacade.updateProfile(profile);

    Map<String, Object> args = new HashMap();
    args.put("userName", profile.getUser().getUserName());
    renderJSON(new RedirectAjaxResponse(Router.reverse("User.view", args).url));
  }

  public static void vote(String entity, Long id) {
    Logger.debug("Voting for [" + entity + "|" + id + "]");
    userFacade.recordUpVote(entity, id, Authorization.getUserFromSessionOrAuthenticate(true));
    renderJSON(new AjaxResponse());
  }

}
