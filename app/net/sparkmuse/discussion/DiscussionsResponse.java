package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.user.UserVotes;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class DiscussionsResponse {

  private final List<Discussion> discussions;
  private final UserVotes votes;

  public DiscussionsResponse(List<Discussion> discussions, UserVotes votes) {
    this.discussions = discussions;
    this.votes = votes;
  }

  public UserVotes getVotes() {
    return votes;
  }

  public List<Discussion> getDiscussions() {
    return discussions;
  }

}
