package net.sparkmuse.ajax;

import net.sparkmuse.common.ResponseCode;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 16, 2010
 */
public class RedirectAjaxResponse extends AjaxResponse {

  private final String targetUrl;

  public RedirectAjaxResponse(final String targetUrl) {
    super(ResponseCode.SEE_OTHER, Type.REDIRECT);
    this.targetUrl = targetUrl;
  }

  public String getTargetUrl() {
    return targetUrl;
  }
}
