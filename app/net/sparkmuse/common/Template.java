package net.sparkmuse.common;

import java.util.Map;

/**
 * @author neteller
 * @created: Feb 10, 2011
 */
public interface Template {

  String getTemplate();

  Map<String, Object> getTemplateArguments();

}
