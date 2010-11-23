package controllers;

import play.mvc.Controller;
import play.data.validation.Required;
import play.data.validation.Validation;

import javax.inject.Inject;

import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.InvalidOAuthRequestToken;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.common.Constants;
import net.sparkmuse.ajax.AjaxResponse;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 8, 2010
 */
public class Authorization extends SparkmuseController {

  @Inject static UserFacade userFacade;

  public static UserVO getUserFromSession(){
    String s = session.get(Constants.SESSION_USER_ID);
    if (StringUtils.isNotBlank(s)) {
      return userFacade.findUserBy(Long.parseLong(s));
    }

    return null;
  }


  public static void authenticate() {
    redirect(userFacade.beginAuthentication());
  }

  public static void authorize(String oauth_token, String oauth_verifier) {
    try {
      UserVO user = userFacade.registerAuthentication(oauth_token, oauth_verifier);
      session.put(Constants.SESSION_USER_ID, user.getId());

      if (user.isAuthorizedFor(AccessLevel.USER)) {
        Home.index();
      }
      else {
        enterInviteCode();
      }
    } catch (InvalidOAuthRequestToken invalidOAuthRequestToken) {
      authenticate();
    }
  }

  public static void enterInviteCode() {
    UserVO user = Authorization.getUserFromSession();
    render(user);
  }

  public static void verifyAuthorizationToken(@Required String authorizationToken) {
    if (Validation.hasErrors()) {
      validation.keep();
      enterInviteCode();
    }

    if (StringUtils.isNotBlank(authorizationToken) && userFacade.verifyAuthorizationToken(Authorization.getUserFromSession(), authorizationToken)) {
      Home.index(); //@todo tutorial window
    }
    else {
      flash.error("The code you entered is invalid.  If you mistyped it, please try again.");
      enterInviteCode();
    }
  }

  public static void applyForInvitation(final String userName, final String url) {
    userFacade.applyForInvitation(userName, url);
    renderJSON(new AjaxResponse());
  }
}