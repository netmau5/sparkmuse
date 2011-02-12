package net.sparkmuse.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.EmailException;
import play.libs.Mail;
import play.templates.Template;
import play.templates.TemplateLoader;
import play.Play;
import play.Logger;

import java.util.Map;

import com.google.common.collect.Maps;
import net.sparkmuse.common.Templates;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class PlayMailService implements MailService {

  public void sendMessage(Email message) {
    if ("true".equals(Play.configuration.getProperty("mail.send"))) {
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
      HtmlEmail email = new HtmlEmail();
      email.addTo(update.getToEmail());
      email.addBcc("dave@sparkmuse.com");
      email.setFrom("noreply@sparkmuse.com", "Sparkmuse");
      email.setSubject(update.getSubject());
      email.setHtmlMsg(content);
      return email;
    } catch (EmailException e) {
      throw new RuntimeException(e);
    }
  }

}
