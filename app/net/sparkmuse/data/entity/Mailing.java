package net.sparkmuse.data.entity;

import org.joda.time.DateTime;
import play.data.validation.Required;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

/**
 * @author neteller
 * @created: Mar 23, 2011
 */
public class Mailing extends Entity<Mailing> {

  @Required
  private String subject;

  private DateTime created;

  @Required
  private DateTime sendDate;

  @Type(Text.class)
  @Required
  private String content;

  private boolean isSent;

  public Mailing() {
    this.created = new DateTime();
  }

  public boolean isSent() {
    return isSent;
  }

  public void setSent(boolean sent) {
    isSent = sent;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public DateTime getSendDate() {
    return sendDate;
  }

  public void setSendDate(DateTime sendDate) {
    this.sendDate = sendDate;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
