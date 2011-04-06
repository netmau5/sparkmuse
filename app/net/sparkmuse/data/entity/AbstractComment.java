package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Store;
import com.google.appengine.api.datastore.Text;
import com.google.common.collect.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import play.data.validation.Required;
import play.data.validation.CheckWith;
import play.data.binding.NoBinding;
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
public abstract class AbstractComment<T extends AbstractComment> extends OwnedEntity<T>
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

  //underlying types must not be immutable for Play validation
  //also, because the way play does object binding, we cannot use a generic type on the setter, so it is abstract
  @Store(false)
  protected List<T> replies; 
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

  public List<T> getReplies() {
    return replies;
  }

  public abstract void setReplies(List<T> replies);

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

  public static <T extends AbstractComment> List<T> applyHierarchy(final List<T> comments) {
    ImmutableMap<Long, T> postById = Maps.uniqueIndex(comments, new Function<T, Long>() {
      public Long apply(T comment) {
        return comment.getId();
      }
    });

    //append any post with an inReplyToId property as a reply to its parent
    for (final T comment: comments) {
      if (null != comment.getInReplyToId()) {
        T parent = postById.get(comment.getInReplyToId());
        if (!parent.getReplies().contains(comment)) {
          parent.setReplies(ImmutableList.<T>builder().addAll(parent.getReplies()).add(comment).build());
        }
      }
    }

    //posts are traversed recursively (getReplies), remove any non-root posts from the returned list
    return Lists.newArrayList(Iterables.filter(comments, new Predicate<T>(){
      public boolean apply(T comment) {
        return null == comment.getInReplyToId();
      }
    }));
  }

}
