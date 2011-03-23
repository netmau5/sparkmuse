package net.sparkmuse.data.entity;

import org.joda.time.DateTime;
import org.apache.commons.lang.StringUtils;

import play.Logger;
import net.sparkmuse.common.CommitmentType;

/**
 * Represents a voter who applied for this idea.
 *
 * @author neteller
 * @created: Mar 20, 2011
 */
public class Commitment extends Entity<Commitment> {

  private Wish wish;
  private Long wishId;
  private UserVO user;
  private Long userId;

  private String email;
  private CommitmentType commitmentType;

  private DateTime created;

  public Commitment() {
    this.created = new DateTime();
  }

  public static Commitment newInstance(Wish wish, UserProfile profile, CommitmentType commitmentType) {
    if (StringUtils.isBlank(profile.getEmail())) {
      throw new IllegalArgumentException("Email is required for a commitment");
    }

    Commitment commitment = new Commitment();
    commitment.wish = wish;
    commitment.wishId = wish.getId();
    commitment.user = profile.getUser();
    commitment.userId = profile.getUser().getId();
    commitment.email = profile.getEmail();
    commitment.commitmentType = commitmentType;
    return commitment;
  }

  public CommitmentType getCommitmentType() {
    return commitmentType;
  }

  public void setCommitmentType(CommitmentType commitmentType) {
    this.commitmentType = commitmentType;
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

  public Long getWishId() {
    return wishId;
  }

  public void setWishId(Long wishId) {
    this.wishId = wishId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
