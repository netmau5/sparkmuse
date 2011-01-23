package net.sparkmuse.mail;

import org.apache.commons.mail.Email;
import play.libs.Mail;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class PlayMailService implements MailService {

  public void sendMessage(Email message) {
    Mail.sendMessage(message);
  }

}
