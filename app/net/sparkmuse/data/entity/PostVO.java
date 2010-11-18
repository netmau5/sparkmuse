package net.sparkmuse.data.entity;

import net.sparkmuse.data.Votable;
import net.sparkmuse.data.mapper.Property;
import net.sparkmuse.data.mapper.Model;
import org.joda.time.DateTime;
import com.google.common.collect.ImmutableList;
import models.PostModel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
@Model(PostModel.class)
public class PostVO extends Entity<PostVO> implements Votable {

  private ImmutableList<PostVO> replies;
  @Property("inReplyToId") private Long inReplyToId;
  @Property("sparkId") private Long sparkId;

  @Property("created") private DateTime created;
  @Property("edited") private DateTime edited;

  @Property("upvotes") private int votes;
  @Property("postContent") private String postContent;

  private UserVO author;

  public PostVO() {
    this.replies = ImmutableList.of();
  }

  public void setAuthor(UserVO author) {
    this.author = author;
  }

  public UserVO getAuthor() {
    return this.author;
  }

  public void upVote() {
    votes += 1;
  }

  public void downVote() {
    votes -= 1;
  }

  public Long getInReplyToId() {
    return inReplyToId;
  }

  public void setInReplyToId(Long inReplyToId) {
    this.inReplyToId = inReplyToId;
  }

  public ImmutableList<PostVO> getReplies() {
    return replies;
  }

  public void setReplies(ImmutableList<PostVO> replies) {
    this.replies = replies;
  }

  public Long getSparkId() {
    return sparkId;
  }

  public void setSparkId(Long sparkId) {
    this.sparkId = sparkId;
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

  public int getVotes() {
    return votes;
  }

  public void setVotes(int votes) {
    this.votes = votes;
  }

  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(String postContent) {
    this.postContent = postContent;
  }
}
