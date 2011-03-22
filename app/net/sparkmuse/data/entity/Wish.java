package net.sparkmuse.data.entity;

import play.data.validation.Required;
import play.data.validation.MaxSize;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Function;
import com.google.common.base.Splitter;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import net.sparkmuse.data.paging.PagingSize;
import net.sparkmuse.user.Votable;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.foundry.HandleWishVoteTask;
import net.sparkmuse.common.CommitmentType;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
@PagingSize(25)
public class Wish extends OwnedEntity<Wish> implements Votable {

  @Required
  @MaxSize(100)
  private String title;

  private List<String> titleWordsLowercase; //for title searching

  @Type(Text.class)
  private String description;

  private List<String> tags;

  private int votes;

  private double rating;

  private int commentCount;

  private boolean notified;

  private DateTime created;
  private DateTime edited;

  public Wish() {
    this.created = new DateTime();
    this.tags = Lists.newArrayList();
    this.titleWordsLowercase = Lists.newArrayList();
  }

  public DateTime getEdited() {
    return edited;
  }

  public void setEdited(DateTime edited) {
    this.edited = edited;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public int getVotes() {
    return votes;
  }

  public void setVotes(int votes) {
    this.votes = votes;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public int getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getTitleWordsLowercase() {
    return titleWordsLowercase;
  }

  public void setTitleWordsLowercase(List<String> titleWordsLowercase) {
    this.titleWordsLowercase = titleWordsLowercase;
  }

  public boolean isNotified() {
    return notified;
  }

  public void setNotified(boolean notified) {
    this.notified = notified;
  }

  public void upVote(UserVote userVote, IssueTaskService issueTaskService) {
    this.votes++;
    commit(issueTaskService, userVote.authorUserId, userVote.entityId, CommitmentType.SEE);
  }

  public static void commit(IssueTaskService issueTaskService, Long committingUserId, Long wishId, CommitmentType commitmentType) {
    issueTaskService.issue(HandleWishVoteTask.class, ImmutableMap.<String, Object>of(
        HandleWishVoteTask.PARAM_VOTER_USER_ID, committingUserId,
        HandleWishVoteTask.PARAM_WISH_ID, wishId,
        HandleWishVoteTask.PARAM_COMMITMENT_TYPE, commitmentType
    ), null);
  }

  public void downVote() {
    this.votes--;
  }

  public Wish lowercaseTags() {
    if (null == this.getTags()) this.setTags(Lists.<String>newArrayList());
    this.setTags(Lists.newArrayList(Iterables.transform(this.getTags(), new Function<String, String>(){
      public String apply(String s) {
        return StringUtils.lowerCase(s);
      }
    })));
    return this;
  }

  public Wish updateTitleTokens() {
    this.setTitleWordsLowercase(Lists.newArrayList(Iterables.transform(Splitter.onPattern("\\s+").split(this.title), new Function<String, String>(){
      public String apply(String s) {
        return StringUtils.lowerCase(s);
      }
    })));
    return this;
  }

}
