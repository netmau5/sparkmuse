package net.sparkmuse.data.entity;

import org.joda.time.DateTime;
import com.google.code.twig.annotation.Type;
import com.google.appengine.api.datastore.Text;
import play.data.validation.Required;
import play.data.validation.CheckWith;
import net.sparkmuse.client.NoScriptCheck;
import net.sparkmuse.user.Votable;
import net.sparkmuse.task.IssueTaskService;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
public class Comment extends OwnedEntity<Comment> implements Votable {

  @Type(Text.class)
  @Required
  private String content;

  @Type(Text.class)
  @Required
  @CheckWith(value= NoScriptCheck.class, message="validation.noscript")
  private String displayContent;

  private DateTime created;
  private DateTime edited;

  private boolean notified;

  @Required
  private Long wishId;

  private int votes;

  public Comment() {
    this.created = new DateTime();
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDisplayContent() {
    return displayContent;
  }

  public void setDisplayContent(String displayContent) {
    this.displayContent = displayContent;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public DateTime getEdited() {
    return edited;
  }

  public void setEdited(DateTime edited) {
    this.edited = edited;
  }

  public Long getWishId() {
    return wishId;
  }

  public void setWishId(Long wishId) {
    this.wishId = wishId;
  }

  public boolean isNotified() {
    return notified;
  }

  public void setNotified(boolean notified) {
    this.notified = notified;
  }

  public int getVotes() {
    return votes;
  }

  public void setVotes(int votes) {
    this.votes = votes;
  }

  public void upVote(UserVote userVote, IssueTaskService issueTaskService) {
    this.votes++;
  }

  public void downVote() {
    this.votes--;
  }
  
}
