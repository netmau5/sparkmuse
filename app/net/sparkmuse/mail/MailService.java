package net.sparkmuse.mail;

import org.apache.commons.mail.Email;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public interface MailService {

  void sendMessage(Email message);
  
}
