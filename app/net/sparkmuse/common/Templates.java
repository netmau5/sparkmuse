package net.sparkmuse.common;

import play.templates.*;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author neteller
 * @created: Feb 10, 2011
 */
public class Templates {

  public static String render(Template template) {
    final play.templates.Template playTemplate = TemplateLoader.load(template.getTemplate());
    final Map<String, Object> args = Maps.newHashMap(template.getTemplateArguments());
    return playTemplate.render(args);
  }

}
