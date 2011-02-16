package net.sparkmuse.data.entity;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Feb 15, 2011
 */
public class Invitation extends Entity<Invitation> {

  private String code;
  private boolean used;
  private DateTime created;

  public Invitation() {
    this.created = new DateTime();
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public boolean isUsed() {
    return used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public static boolean isValid(Invitation invitation) {
    return null != invitation && !invitation.isUsed();
  }

}
