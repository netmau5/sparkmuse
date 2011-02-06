package net.sparkmuse.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.EmailException;
import play.libs.Mail;
import play.templates.Template;
import play.templates.TemplateLoader;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class PlayMailService implements MailService {

  public void sendMessage(Email message) {
    Mail.send(message);
  }

  public void prepareAndSendMessage(EmailTemplate template) {
    sendMessage(prepareEmail(template));
  }

  Email prepareEmail(EmailTemplate update) {
    final Template template = TemplateLoader.load(update.getTemplate());
    final Map<String, Object> args = Maps.newHashMap();
    args.put("update", update);
    final String content = template.render(args);

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
