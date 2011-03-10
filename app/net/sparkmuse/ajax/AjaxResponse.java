package net.sparkmuse.ajax;

import net.sparkmuse.common.ResponseCode;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 9, 2010
 */
public class AjaxResponse {

  private int status;
  private Type type;

  public AjaxResponse() {
    this(ResponseCode.OK, Type.SUCCESS);
  }

  public AjaxResponse(ResponseCode responseCode, Type type){
    this.status = responseCode.getStatusCode();
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  public int getStatus() {
    return status;
  }

  public enum Type {
    SUCCESS,
    VALIDATION_ERROR,
    SYSTEM_ERROR,
    REDIRECT,
    FRAGMENT,
    JSON,
    INVALID_REQUEST_ERROR; //user request that we couldn't handle, not a severe error
  }
}
