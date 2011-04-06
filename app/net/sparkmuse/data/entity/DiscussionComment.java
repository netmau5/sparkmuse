package net.sparkmuse.data.entity;

import com.google.common.collect.Lists;

import java.util.List;

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

  //AccessControlException thrown on GAE when this returned immutablelist...
  public void setReplies(List<DiscussionComment> replies) {
    this.replies = Lists.newArrayList(replies);
  }

}
