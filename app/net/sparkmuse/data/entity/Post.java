package net.sparkmuse.data.entity;

import net.sparkmuse.common.NullTo;

import java.util.List;

import play.data.validation.Required;
import com.google.common.collect.Lists;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Post extends AbstractComment<Post> {

  @Required
  private Long sparkId;

  private List<Visual> visuals;

//  @CheckWith(value= CollectionMemberCheck.class)
  private List<Resource> resources;
  
  private List<Offer> offers;
  private String leadingQuestion;

  public Long getSparkId() {
    return sparkId;
  }

  public void setSparkId(Long sparkId) {
    this.sparkId = sparkId;
  }

  public List<Visual> getVisuals() {
    return NullTo.empty(visuals);
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

  //AccessControlException thrown on GAE when this returned immutablelist...
  public void setReplies(List<Post> replies) {
    this.replies = Lists.newArrayList(replies);
  }
}
