package net.sparkmuse.mail;

import net.sparkmuse.common.Template;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public interface EmailTemplate extends Template {

  /**
   * Email address of recipient.
   *
   * @return
   */
  String getToEmail();

  /**
   * Subject line
   *
   * @return
   */
  String getSubject();

  /**
   * Person this email is being sent to.
   *
   * @return
   */
  String getUpdateeName();

}
