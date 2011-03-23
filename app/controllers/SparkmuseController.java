package controllers;

import play.mvc.*;
import play.Logger;
import play.data.validation.Validation;
import net.sparkmuse.ajax.*;
import net.sparkmuse.common.ResponseCode;
import net.sparkmuse.common.Constants;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.TwitterAuthenticationException;
import net.sparkmuse.data.entity.UserProfile;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.DeadlineExceededException;
import com.google.code.twig.ObjectDatastore;

/**
 * Base controller class for all Sparkmuse Play Controllers.
 *
 * @author neteller
 * @created: Aug 21, 2010
 */
public class SparkmuseController extends Controller {

  @Inject static UserFacade userFacade;
  @Inject static ObjectDatastore datastore;

  @Catch(TwitterAuthenticationException.class)
  static void handleException(TwitterAuthenticationException e) {
    String message = e.getMessage();
    Logger.error(e, message);
    if (request.isAjax()) {
      renderJSON(new RedirectAjaxResponse(Router.reverse("Authorization.authenticate").url));
    }
    renderTemplate("Application/twitterLoginError.html", message);
  }

  @Catch(ApiProxy.CapabilityDisabledException.class)
  static void handleMaintenanceMode(ApiProxy.CapabilityDisabledException e) {
    Logger.error(e, "App engine disabled.");
    handleException(new InvalidRequestException("Our data center has gone offline and we cannot handle new requests."));
  }

  @Catch(DeadlineExceededException.class)
  static void handleDeadlineExceeded(DeadlineExceededException e) throws Exception {
    Logger.error(e, "Deadline Exceeded");
    if (request.isAjax()) {
      handleException(new InvalidRequestException("Your request timed out, please try again."));
    }
    //@todo handle else
    throw new Exception(e);
  }

  @Catch(InvalidRequestException.class)
  static void handleException(InvalidRequestException e) {
    String message = e.getMessage();
    Logger.info(e, message);

    if (request.isAjax()) {
      renderJSON(new InvalidRequestErrorAjaxResponse(message));
    }
    else {
      renderTemplate("Application/error.html", message);
    }
  }

  @Catch(Exception.class)
  static void handleException(Exception e) throws Exception {
    Logger.error(e, "Unhandled Exception");
    if (ResponseCode.INTERNAL_SERVER_ERROR.getStatusCode() == response.status && request.isAjax()) {
      renderJSON(new AjaxResponse(ResponseCode.forStatusCode(response.status), AjaxResponse.Type.SYSTEM_ERROR));
    }
    throw e;
  }

  @Before
  static void fillCurrentUserRenderArg() {
    final String userId = session.get(Constants.SESSION_USER_ID);
    if (StringUtils.isNotBlank(userId)) {
      UserProfile userProfile = userFacade.findUserProfileBy(Long.valueOf(userId));
      renderArgs.put("currentUser", null != userProfile ? userProfile.getUser() : null);
      renderArgs.put("currentUserProfile", userProfile);
    }
  }

  @Before
  static void invalidDataOnAjaxRequest() {
    if (request.isAjax() && Validation.hasErrors()) {
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }
  }

  @After
  static void clearDatastoreCache() {
    datastore.disassociateAll();
  }
  
}
