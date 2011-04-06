package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.data.entity.DiscussionContent;
import net.sparkmuse.user.UserVotes;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class DiscussionResponse {

  private final Discussion discussion;
  private final DiscussionContent content;
  private final Comments comments;
  private final UserVotes votes;

  public DiscussionResponse(Discussion discussion, DiscussionContent content, Comments comments, UserVotes votes) {
    this.discussion = discussion;
    this.content = content;
    this.comments = comments;
    this.votes = votes;
  }

  public Discussion getDiscussion() {
    return discussion;
  }

  public DiscussionContent getContent() {
    return content;
  }

  public Comments getComments() {
    return comments;
  }

  public UserVotes getVotes() {
    return votes;
  }
}
