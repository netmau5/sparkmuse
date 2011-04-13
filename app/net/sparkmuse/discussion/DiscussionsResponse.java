package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.data.entity.DiscussionGroup;
import net.sparkmuse.user.UserVotes;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class DiscussionsResponse {

  private final List<Discussion> discussions;
  private final UserVotes userVotes;
  private final DiscussionGroup group;

  public DiscussionsResponse(List<Discussion> discussions, UserVotes userVotes, DiscussionGroup group) {
    this.discussions = discussions;
    this.userVotes = userVotes;
    this.group = group;
  }

  public UserVotes getUserVotes() {
    return userVotes;
  }

  public List<Discussion> getDiscussions() {
    return discussions;
  }

  public DiscussionGroup getGroup() {
    return group;
  }

}
