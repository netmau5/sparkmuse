package net.sparkmuse.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import play.libs.Mail;
import play.templates.Template;
import play.templates.TemplateLoader;
import play.Play;
import play.Logger;

import java.util.Map;
import java.util.List;

import com.google.common.collect.Maps;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Query;
import com.google.inject.Inject;
import net.sparkmuse.common.Templates;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.data.entity.Mailing;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.twig.DatastoreUtils;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class PlayMailService implements MailService {

  private final ObjectDatastore datastore;

  @Inject
  public PlayMailService(ObjectDatastore datastore) {
    this.datastore = datastore;
  }

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

  public Mailing save(Mailing newMailing) {
    if (newMailing.getId() != null) {
      Mailing existingMailing = datastore.load(Mailing.class, newMailing.getId());
      newMailing = Reflections.overlay(existingMailing, newMailing);
    }
    return DatastoreUtils.storeOrUpdate(newMailing, datastore);
  }

  public List<Mailing> getAllMailings() {
    return datastore.find().type(Mailing.class)
        .addSort("created", Query.SortDirection.DESCENDING)
        .returnAll()
        .now();
  }

  public Mailing getMailingBy(Long id) {
    return datastore.load(Mailing.class, id);
  }

  public List<Mailing> mailingsFor(DateTime date) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void sendMailing(Mailing mailing, UserVO user) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
