package net.sparkmuse.client;

import java.util.regex.Pattern;

import play.data.validation.Check;

/**
 * @author neteller
 * @created: Jan 8, 2011
 */
public class NoScriptCheck extends Check {
  
  private final Pattern scriptPattern = Pattern.compile(".*</?script.*", Pattern.DOTALL);

  public boolean isSatisfied(Object propertyOwner, Object property) {
    if (null == property) return true;

    final String text = property.toString();
    return !scriptPattern.matcher(text).matches();
  }

}
