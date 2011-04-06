package net.sparkmuse.data.entity;

import net.sparkmuse.common.DiscussionType;
import net.sparkmuse.common.Dateable;
import net.sparkmuse.client.NoScriptCheck;
import net.sparkmuse.activity.Notifiable;
import net.sparkmuse.user.Votable;
import net.sparkmuse.task.IssueTaskService;
import com.google.code.twig.annotation.Type;
import com.google.appengine.api.datastore.Text;
import play.data.validation.Required;
import play.data.validation.CheckWith;
import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Apr 4, 2011
 */
public class Discussion extends OwnedEntity<Discussion> 
    implements Votable, Dateable, Notifiable {

  private String title;

  //for DiscussionType.LINK

  private String url;

  //for DiscussionType.DISCUSS

  @Type(Text.class)
  @Required
  private String content;

  @Type(Text.class)
  @Required
  @CheckWith(value= NoScriptCheck.class, message="validation.noscript")
  private String displayContent;

  private DiscussionType discussionType;

  private DateTime created;
  private DateTime edited;
  private int votes;
  private boolean notified;
  private int commentCount;

  public Discussion() {
    this.created = new DateTime();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public DiscussionType getDiscussionType() {
    return discussionType;
  }

  public void setDiscussionType(DiscussionType discussionType) {
    this.discussionType = discussionType;
  }

  public int getVotes() {
    return votes;
  }

  public void setVotes(int votes) {
    this.votes = votes;
  }

  public boolean isNotified() {
    return notified;
  }

  public void setNotified(boolean notified) {
    this.notified = notified;
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

  public void upVote(UserVote userVote, IssueTaskService issueTaskService) {
    votes += 1;
  }

  public void downVote() {
    votes -= 1;
  }

  public int getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }
}
