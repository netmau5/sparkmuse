package net.sparkmuse.data.entity;

import org.joda.time.DateTime;
import play.data.validation.Required;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

/**
 * @author neteller
 * @created: Mar 23, 2011
 */
public class Email extends Entity<Email> {

  @Required
  private String name;

  private DateTime created;

  @Required
  private DateTime sendDate;

  @Type(Text.class)
  @Required
  private String content;

  public Email() {
    this.created = new DateTime();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
