package net.sparkmuse.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import play.libs.Mail;
import play.Play;
import play.Logger;

import java.util.List;

import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Query;
import com.google.inject.Inject;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import net.sparkmuse.common.Templates;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.data.entity.Mailing;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.twig.DatastoreUtils;

import javax.mail.internet.InternetAddress;

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
    return datastore.find().type(Mailing.class)
        .addFilter("sendDate", Query.FilterOperator.LESS_THAN, date.getMillis())
        .addFilter("isSent", Query.FilterOperator.EQUAL, false)
        .addSort("sendDate", Query.SortDirection.DESCENDING)
        .returnAll()
        .now();
  }

  public void sendMailing(Long mailingId, UserProfile userProfile) {
    if (userProfile.hasEmail()) {
      Mailing mailing = getMailingBy(mailingId);
      prepareAndSendMessage(new MailingTemplate(mailing, userProfile));
    }
  }

  public void markSent(List<Mailing> mailingList) {
    datastore.updateAll(Lists.newArrayList(Iterables.transform(mailingList, new Function<Mailing, Mailing>(){
      public Mailing apply(Mailing mailing) {
        mailing.setSent(true);
        DatastoreUtils.associate(mailing, datastore);
        return mailing;
      }
    })));
  }
}
