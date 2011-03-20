package net.sparkmuse.data.entity;

import play.data.validation.Required;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import net.sparkmuse.data.paging.PagingSize;
import net.sparkmuse.user.Votable;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
@PagingSize(25)
public class Wish extends OwnedEntity<Wish> implements Votable {

  @Required
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

  public boolean isNotified() {
    return notified;
  }

  public void setNotified(boolean notified) {
    this.notified = notified;
  }

  public void upVote() {
    this.votes++;
  }

  public void downVote() {
    this.votes--;
  }

  public Wish lowercaseTags() {
    this.setTags(Lists.newArrayList(Iterables.transform(this.getTags(), new Function<String, String>(){
      public String apply(String s) {
        return StringUtils.lowerCase(s);
      }
    })));
    return this;
  }

}
