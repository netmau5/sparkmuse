package net.sparkmuse.data.entity;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class DiscussionComment extends AbstractComment<DiscussionComment> {

  private Long discussionId;

  public Long getDiscussionId() {
    return discussionId;
  }

  public void setDiscussionId(Long discussionId) {
    this.discussionId = discussionId;
  }

}
