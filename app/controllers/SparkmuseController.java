package controllers;

import play.mvc.Controller;
import play.mvc.Catch;
import play.Logger;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.common.ResponseCode;

/**
 * Base controller class for all Sparkmuse Play Controllers.
 *
 * @author neteller
 * @created: Aug 21, 2010
 */
public class SparkmuseController extends Controller {

  @Catch
  static void handleException(Exception e) {
    Logger.error(e, "Unhandled Exception");
    if (ResponseCode.INTERNAL_SERVER_ERROR.getStatusCode() == response.status && request.isAjax()) {
      renderJSON(new AjaxResponse(ResponseCode.forStatusCode(response.status), AjaxResponse.Type.SYSTEM_ERROR));
    }
    //otherwise the user should be redirected to errors/500.html
  }

}
