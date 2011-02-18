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

  //for group invites
  private int totalInvites = 1;
  private int usedInvites;
  private String groupName;
  private String groupImage;

  public Invitation() {
    this.created = new DateTime();
  }

  public Invitation useInvite() {
    this.usedInvites++;
    if (usedInvites == totalInvites) used = true;
    return this;
  }

  public int getTotalInvites() {
    return totalInvites;
  }

  public void setTotalInvites(int totalInvites) {
    this.totalInvites = totalInvites;
  }

  public int getUsedInvites() {
    return usedInvites;
  }

  public void setUsedInvites(int usedInvites) {
    this.usedInvites = usedInvites;
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

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupImage() {
    return groupImage;
  }

  public void setGroupImage(String groupImage) {
    this.groupImage = groupImage;
  }

  public static boolean isValid(Invitation invitation) {
    return null != invitation && !invitation.isUsed();
  }

}
