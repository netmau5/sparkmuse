package net.sparkmuse.ajax;

import net.sparkmuse.common.ResponseCode;

/**
 * @author neteller
 * @created: Mar 9, 2011
 */
public class JsonAjaxResponse extends AjaxResponse {

  private final Object data;

  public JsonAjaxResponse(Object data) {
    super(ResponseCode.OK, Type.JSON);
    this.data = data;
  }

  public Object getData() {
    return data;
  }

}
