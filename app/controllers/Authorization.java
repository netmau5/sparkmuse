package controllers;

import play.mvc.Router;
import play.data.validation.Validation;
import play.data.validation.Required;

import javax.inject.Inject;

import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.OAuthAuthenticationRequest;
import net.sparkmuse.user.OAuthAuthenticationResponse;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.UserApplication;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.common.Constants;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import org.apache.commons.lang.StringUtils;
import twitter4j.http.RequestToken;

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
      return userFacade.findUserBy(Long.valueOf(s));
    }

    return null;
  }

  public static UserVO getUserFromSessionOrAuthenticate(boolean isAjax) {
    final UserVO user = getUserFromSession();
    if (user != null) return user;

    if (isAjax) {
      renderJSON(new RedirectAjaxResponse(Router.reverse("Authorization.authenticate").url));
    }
    else {
      authenticate();
    }

    return null;
  }


  public static void authenticate() {
    final OAuthAuthenticationRequest request = userFacade.beginAuthentication();
    session.put(Constants.REQUEST_TOKEN, request.getToken());
    session.put(Constants.REQUEST_TOKEN_SECRET, request.getTokenSecret());
    redirect(request.getAuthorizationUrl());
  }

  public static void authorize(String oauth_token, String oauth_verifier) {
    final OAuthAuthenticationResponse response = new OAuthAuthenticationResponse(
        new RequestToken(session.get(Constants.REQUEST_TOKEN), session.get(Constants.REQUEST_TOKEN_SECRET)),
        oauth_token,
        oauth_verifier
    );
    UserVO user = userFacade.registerAuthentication(response, session.get(Constants.INVITATION_CODE));
    session.put(Constants.SESSION_USER_ID, user.getId());

    if (user.isAuthorizedFor(AccessLevel.USER)) {
      if (user.isNewUser()) {
        Home.welcome();
      }
      else {
        Home.index();
      }
    }
    else {
      unauthorized();
    }
  }

  public static void unauthorized() {
    render();
  }

  public static void applyForInvitation(UserApplication application) {
    userFacade.applyForInvitation(application);
    renderJSON(new AjaxResponse());
  }

  public static void applyInvitation(@Required(message="validation.required.invitationCode") String invitationCode) {
    if (!userFacade.verifyInvitationCode()) {
      Validation.addError("invitationCode", "Could not verify code, please check if it was entered correctly.");
    }

    if (validation.hasErrors()) {
      params.flash();
      validation.keep();
      Application.invitation();
    }

    session.put(Constants.INVITATION_CODE, invitationCode);
    authenticate();
  }
}
