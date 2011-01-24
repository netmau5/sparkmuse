package net.sparkmuse.mail;

import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public interface ActivityUpdate {

  String getToEmail();

  String getSubject();

  String getTemplate();

  String getUpdateeName();

}
