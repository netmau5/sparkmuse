package net.sparkmuse.mail;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public interface EmailTemplate {

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
   * Template of email
   *
   * @return
   */
  String getTemplate();

  /**
   * Person this email is being sent to.
   *
   * @return
   */
  String getUpdateeName();

}
