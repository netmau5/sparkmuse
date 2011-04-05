package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Store;
import com.google.appengine.api.datastore.Text;
import com.google.common.collect.Lists;
import play.data.validation.Required;
import play.data.validation.CheckWith;
import org.joda.time.DateTime;
import net.sparkmuse.client.NoScriptCheck;
import net.sparkmuse.activity.Notifiable;
import net.sparkmuse.common.Dateable;
import net.sparkmuse.user.Votable;
import net.sparkmuse.task.IssueTaskService;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 4, 2011
 */
public class AbstractComment<T> extends OwnedEntity<T>
    implements Votable, Dateable, Notifiable {

  private DateTime created;
  private DateTime edited;

  private int votes;
  private boolean notified;

  @Type(Text.class)
  @Required
  private String content;

  @Type(Text.class)
  @Required
  @CheckWith(value= NoScriptCheck.class, message="validation.noscript")
  private String displayContent;

  @Store(false) private List<Post> replies; //underlying types must not be immutable for Play validation
  private Long inReplyToId;

  public AbstractComment() {
    this.replies = Lists.newArrayList();
    this.created = new DateTime();
  }

  public void upVote(UserVote userVote, IssueTaskService issueTaskService) {
    votes += 1;
  }

  public void downVote() {
    votes -= 1;
  }

  public int getVotes() {
    return votes;
  }

  //AccessControlException thrown on GAE when this returned immutablelist...
  public List<Post> getReplies() {
    return Lists.newArrayList(replies);
  }

  public boolean isNotified() {
    return notified;
  }

  public DateTime getCreated() {
    return created;
  }

  public DateTime getEdited() {
    return edited;
  }

  public String getContent() {
    return content;
  }

  public String getDisplayContent() {
    return displayContent;
  }

  public Long getInReplyToId() {
    return inReplyToId;
  }

  public void setReplies(List<Post> replies) {
    this.replies = replies;
  }

  public void setInReplyToId(Long inReplyToId) {
    this.inReplyToId = inReplyToId;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public void setEdited(DateTime edited) {
    this.edited = edited;
  }

  public void setVotes(int votes) {
    this.votes = votes;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setDisplayContent(String displayContent) {
    this.displayContent = displayContent;
  }

  public void setNotified(boolean notified) {
    this.notified = notified;
  }


}
