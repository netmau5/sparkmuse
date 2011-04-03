package net.sparkmuse.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.EmailException;
import play.libs.Mail;
import play.Play;
import play.Logger;

import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import net.sparkmuse.common.Templates;

import javax.mail.internet.InternetAddress;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class PlaySendMailService implements SendMailService {

  private final ObjectDatastore datastore;

  @Inject
  public PlaySendMailService(ObjectDatastore datastore) {
    this.datastore = datastore;
  }

  public void sendMessage(Email message) {
    if ("true".equals(Play.configuration.getProperty("mail.send")) ||
        ((InternetAddress) message.getToAddresses().get(0)).getAddress().toLowerCase().equals("dave@sparkmuse.com")) {
      Mail.send(message);
    }
    else {
      Logger.info("Mail service received message for sending but [mail.send] configuration disabled: " + message);
    }
  }

  public void prepareAndSendMessage(EmailTemplate template) {
    sendMessage(prepareEmail(template));
  }

  Email prepareEmail(EmailTemplate update) {
    final String content = Templates.render(update);

    try {
      Logger.info("Preparing email update to [" + update.getToEmail() + "] titled [" + update.getSubject() + "]");
      HtmlEmail email = new HtmlEmail();
      email.addTo(update.getToEmail());
//      email.addBcc("dave@sparkmuse.com");
      email.setFrom("noreply@sparkmuse.com", "Sparkmuse");
      email.setSubject(update.getSubject());
      email.setHtmlMsg(content);
      return email;
    } catch (EmailException e) {
      throw new RuntimeException(e);
    }
  }


}
