package controllers;

import play.mvc.*;
import play.Logger;
import play.Play;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.common.ResponseCode;
import net.sparkmuse.common.Constants;
import net.sparkmuse.user.UserFacade;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import com.google.common.base.Function;

/**
 * Base controller class for all Sparkmuse Play Controllers.
 *
 * @author neteller
 * @created: Aug 21, 2010
 */
public class SparkmuseController extends Controller {

  @Inject static UserFacade userFacade;

  @Catch
  static void handleException(Exception e) {
    Logger.error(e, "Unhandled Exception");
    if (ResponseCode.INTERNAL_SERVER_ERROR.getStatusCode() == response.status && request.isAjax()) {
      renderJSON(new AjaxResponse(ResponseCode.forStatusCode(response.status), AjaxResponse.Type.SYSTEM_ERROR));
    }
    //otherwise the user should be redirected to errors/500.html
  }

  @Before
  static void fillCurrentUserRenderArg() {
    final String userId = session.get(Constants.SESSION_USER_ID);
    if (StringUtils.isNotBlank(userId)) {
      renderArgs.put("currentUser", userFacade.findUserBy(Long.valueOf(userId)));
    }
  }
  
}
