package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.UserVO;

/**
 * @author neteller
 * @created: Apr 9, 2011
 */
public class DiscussionsRequest {

  private String groupName;
  private UserVO requestingUser;

  public DiscussionsRequest(UserVO requestingUser) {
    this.requestingUser = requestingUser;
  }

  public DiscussionsRequest forGroup(String groupName) {
    this.groupName = groupName;
    return this;
  }

  public String getGroupName() {
    return groupName;
  }

  public UserVO getRequestingUser() {
    return requestingUser;
  }
}
