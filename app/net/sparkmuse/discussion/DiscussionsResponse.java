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
  private final UserVotes userVotes;

  public DiscussionsResponse(List<Discussion> discussions, UserVotes userVotes) {
    this.discussions = discussions;
    this.userVotes = userVotes;
  }

  public UserVotes getUserVotes() {
    return userVotes;
  }

  public List<Discussion> getDiscussions() {
    return discussions;
  }

}
