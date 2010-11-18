package controllers;

import play.mvc.Controller;
import play.mvc.Catch;
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
  public static void handleException() {
    if (ResponseCode.INTERNAL_SERVER_ERROR.getStatusCode() == response.status &&
        request.isAjax()) {
      //@todo log error
      renderJSON(new AjaxResponse(ResponseCode.forStatusCode(response.status), AjaxResponse.Type.SYSTEM_ERROR));
    }
    //otherwise the user should be redirected to errors/500.html
  }

}
