package net.sparkmuse.mail;

import org.apache.commons.mail.Email;
import org.joda.time.DateTime;
import net.sparkmuse.data.entity.Mailing;
import net.sparkmuse.data.entity.UserProfile;

import java.util.List;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public interface MailService {

  void sendMessage(Email message);

  void prepareAndSendMessage(EmailTemplate template);

  Mailing save(Mailing mailing);
  List<Mailing> getAllMailings();
  List<Mailing> mailingsFor(DateTime date);
  void sendMailing(Long mailingId, UserProfile userProfile);
  Mailing getMailingBy(Long id);
}
