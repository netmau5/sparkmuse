package net.sparkmuse.ajax;

import net.sparkmuse.common.ResponseCode;

/**
 * @author neteller
 * @created: Jan 25, 2011
 */
public class InvalidRequestErrorAjaxResponse extends AjaxResponse {

  private final String message;

  public InvalidRequestErrorAjaxResponse(String message) {
    super(ResponseCode.CONFLICT, AjaxResponse.Type.INVALID_REQUEST_ERROR);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
  
}
