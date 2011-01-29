package controllers;

import play.mvc.*;
import play.Logger;
import play.data.validation.Validation;
import net.sparkmuse.ajax.*;
import net.sparkmuse.common.ResponseCode;
import net.sparkmuse.common.Constants;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.TwitterLoginExpiredException;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import com.google.apphosting.api.ApiProxy;
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

  @Catch(TwitterLoginExpiredException.class)
  static void handleException(TwitterLoginExpiredException e) {
    Logger.error(e, "Twitter Login Expired");
    if (request.isAjax()) {
      renderJSON(new RedirectAjaxResponse(Router.reverse("Authorization.authenticate").url));
    }
    Authorization.authenticate();
  }

  @Catch(ApiProxy.CapabilityDisabledException.class)
  static void handleMaintenanceMode(ApiProxy.CapabilityDisabledException e) {
    Logger.error(e, "App engine disabled.");
    //@todo handle this error somewhere, it will be thrown when GAE goes into read-only mode for scheduled maintenance
  }

  @Catch(InvalidRequestException.class)
  static void handleException(InvalidRequestException e) throws Exception {
    Logger.info(e, e.getMessage());
    if (request.isAjax()) {
      renderJSON(new InvalidRequestErrorAjaxResponse(e.getMessage()));
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
      renderArgs.put("currentUser", userFacade.findUserBy(Long.valueOf(userId)));
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
