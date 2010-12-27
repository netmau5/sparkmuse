package net.sparkmuse.ajax;

import net.sparkmuse.common.ResponseCode;

/**
 * @author neteller
 * @created: Dec 26, 2010
 */
public class FragmentAjaxResponse extends AjaxResponse {

  private final String fragment; //markup

  public FragmentAjaxResponse(String fragment) {
    super(ResponseCode.OK, Type.FRAGMENT);
    this.fragment = fragment;
  }

  public String getFragment() {
    return fragment;
  }

}
