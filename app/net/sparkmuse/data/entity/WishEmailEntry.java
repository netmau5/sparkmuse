package net.sparkmuse.data.entity;

import org.joda.time.DateTime;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Id;

import com.google.common.base.Preconditions;
import play.Logger;

/**
 * Represents a voter who applied for this idea.
 *
 * @author neteller
 * @created: Mar 20, 2011
 */
public class WishEmailEntry extends Entity<WishEmailEntry> {

  private Wish wish;
  private UserVO user;

  private String email;

  private DateTime created;

  public WishEmailEntry() {
    this.created = new DateTime();
  }

  public static WishEmailEntry newInstance(Wish wish, UserProfile profile) {
    if (StringUtils.isBlank(profile.getEmail())) {
      Logger.error("Email is required for a WishEmailEntry");
      return null;
    }

    WishEmailEntry emailEntry = new WishEmailEntry();
    emailEntry.wish = wish;
    emailEntry.user = profile.getUser();
    emailEntry.email = profile.getEmail();
    return emailEntry;
  }

  public Wish getWish() {
    return wish;
  }

  public void setWish(Wish wish) {
    this.wish = wish;
  }

  public UserVO getUser() {
    return user;
  }

  public void setUser(UserVO user) {
    this.user = user;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }
}
