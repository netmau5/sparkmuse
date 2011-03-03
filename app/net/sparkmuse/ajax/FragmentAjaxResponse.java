package net.sparkmuse.ajax;

import net.sparkmuse.common.ResponseCode;

import java.util.Map;
import java.util.HashMap;

import play.templates.Template;
import play.templates.TemplateLoader;
import com.google.common.collect.Maps;

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

  public FragmentAjaxResponse(String templatePath, Map<String, Object> arguments) {
    HashMap<String,Object> map = Maps.newHashMap(arguments); //make sure this is a read/write
    final Template template = TemplateLoader.load(templatePath);
    this.fragment = template.render(map);
  }

  public String getFragment() {
    return fragment;
  }

}
