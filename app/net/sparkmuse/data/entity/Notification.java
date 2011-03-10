package net.sparkmuse.data.entity;

import org.joda.time.DateTime;
import com.google.code.twig.annotation.Id;

/**
 * @author neteller
 * @created: Mar 8, 2011
 */
public class Notification {

  private Long id; //we generate this
  private String displayMessage;
  private DateTime created;
  private DateTime expireDate;

  public Notification() {
    this.created = new DateTime();
    this.id = System.currentTimeMillis();
  }

  public Notification(String displayMessage) {
    this();
    this.displayMessage = displayMessage;
  }

  public String getDisplayMessage() {
    return displayMessage;
  }

  public void setDisplayMessage(String displayMessage) {
    this.displayMessage = displayMessage;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public DateTime getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(DateTime expireDate) {
    this.expireDate = expireDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
