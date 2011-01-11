package controllers;

import net.sparkmuse.user.UserFacade;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.ajax.AjaxResponse;

import javax.inject.Inject;

import play.Logger;


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
    render(profile);
  }

  public static void edit(String userName) {
    final UserProfile profile = userFacade.getUserProfile(userName);
    render(profile);
  }

  public static void vote(String entity, Long id) {
    Logger.debug("Voting for [" + entity + "|" + id + "]");
    userFacade.recordUpVote(entity, id, Authorization.getUserFromSessionOrAuthenticate(true));
    renderJSON(new AjaxResponse());
  }

}
