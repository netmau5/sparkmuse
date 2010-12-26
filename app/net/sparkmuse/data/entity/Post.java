package net.sparkmuse.data.entity;

import net.sparkmuse.user.Votable;
import org.joda.time.DateTime;
import com.google.common.collect.ImmutableList;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

import java.util.List;

import play.data.validation.Required;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Post extends OwnedEntity<Post> implements Votable {

  private ImmutableList<Post> replies;
  private Long inReplyToId;

  @Required
  private Long sparkId;

  private DateTime created;
  private DateTime edited;

  private int votes;
  @Type(Text.class) private String content;
  @Type(Text.class) private String displayContent;

  private List<Visual> visuals;

  public Post() {
    this.replies = ImmutableList.of();
    this.created = new DateTime();
  }

  public void upVote() {
    votes += 1;
  }

  public void downVote() {
    votes -= 1;
  }

  public int getVotes() {
    return votes;
  }

  public ImmutableList<Post> getReplies() {
    return replies;
  }

  public Long getInReplyToId() {
    return inReplyToId;
  }

  public Long getSparkId() {
    return sparkId;
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

  public List<Visual> getVisuals() {
    return visuals;
  }

  public void setReplies(ImmutableList<Post> replies) {
    this.replies = replies;
  }

  public void setInReplyToId(Long inReplyToId) {
    this.inReplyToId = inReplyToId;
  }

  public void setSparkId(Long sparkId) {
    this.sparkId = sparkId;
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

  public void setVisuals(List<Visual> visuals) {
    this.visuals = visuals;
  }
}
