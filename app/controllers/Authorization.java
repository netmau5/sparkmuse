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
import net.sparkmuse.data.entity.Invitation;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.common.Constants;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import net.sparkmuse.ajax.InvalidRequestException;
import org.apache.commons.lang.StringUtils;
import twitter4j.http.RequestToken;
import controllers.Foundry;

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
    if (StringUtils.isBlank(oauth_token) && StringUtils.isBlank(oauth_verifier) &&
        StringUtils.isNotBlank(request.params.get("denied"))) {
      Application.farewell();
    }

    String requestToken = session.get(Constants.REQUEST_TOKEN);
    if (StringUtils.isEmpty(requestToken)) {
      throw new InvalidRequestException("It appears you have cookies disabled. We cannot presently process logins without them.");
    }

    final OAuthAuthenticationResponse response = new OAuthAuthenticationResponse(
        new RequestToken(requestToken, session.get(Constants.REQUEST_TOKEN_SECRET)),
        oauth_token,
        oauth_verifier
    );
    UserVO user = userFacade.registerAuthentication(response, session.get(Constants.INVITATION_CODE));
    session.put(Constants.SESSION_USER_ID, user.getId());

    if (user.isAuthorizedFor(AccessLevel.FOUNDRY)) {
      if (user.isNewUser()) {
        Home.welcome();
      }
      else if (user.isUser()) {
        Home.index();
      }
      else {
        Foundry.index(1);
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
    Invitation invitation = userFacade.verifyInvitationCode(invitationCode);
    if (null == invitation) {
      Validation.addError("invitationCode", "Could not verify code, please check if it was entered correctly.");
    }
    else if (invitation.isUsed()) {
      Validation.addError("invitationCode", "Code is valid but it has already been used.");
    }

    if (validation.hasErrors()) {
      params.flash();
      validation.keep();
      Application.invitation(null);
    }

    session.put(Constants.INVITATION_CODE, invitationCode);
    authenticate();
  }
}
