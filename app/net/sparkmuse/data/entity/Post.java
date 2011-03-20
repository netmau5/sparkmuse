package net.sparkmuse.data.entity;

import net.sparkmuse.user.Votable;
import net.sparkmuse.common.Dateable;
import net.sparkmuse.common.NullTo;
import net.sparkmuse.client.NoScriptCheck;
import net.sparkmuse.activity.Notifiable;
import net.sparkmuse.task.IssueTaskService;
import org.joda.time.DateTime;
import com.google.common.collect.Lists;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Store;

import java.util.List;

import play.data.validation.Required;
import play.data.validation.CheckWith;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Post extends OwnedEntity<Post>
    implements Votable, Dateable, Notifiable {

  @Store(false) private List<Post> replies; //underlying types must not be immutable for Play validation
  private Long inReplyToId;

  @Required
  private Long sparkId;

  private DateTime created;
  private DateTime edited;

  private int votes;

  @Type(Text.class)
  @Required
  private String content;

  @Type(Text.class)
  @Required
  @CheckWith(value= NoScriptCheck.class, message="validation.noscript")
  private String displayContent;

  private List<Visual> visuals;

//  @CheckWith(value= CollectionMemberCheck.class)
  private List<Resource> resources;
  
  private List<Offer> offers;
  private String leadingQuestion;

  private boolean notified;

  public Post() {
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
    return NullTo.empty(visuals);
  }

  public void setReplies(List<Post> replies) {
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

  public List<Resource> getResources() {
    return NullTo.empty(resources);
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }

  public List<Offer> getOffers() {
    return NullTo.empty(offers);
  }

  public void setOffers(List<Offer> offers) {
    this.offers = offers;
  }

  public String getLeadingQuestion() {
    return leadingQuestion;
  }

  public void setLeadingQuestion(String leadingQuestion) {
    this.leadingQuestion = leadingQuestion;
  }

  public boolean isNotified() {
    return notified;
  }

  public void setNotified(boolean notified) {
    this.notified = notified;
  }
  
}
