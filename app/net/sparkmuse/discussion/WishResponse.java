package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.Comment;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.user.UserVotes;

import java.util.List;

/**
 * @author neteller
 * @created: Mar 21, 2011
 */
public class WishResponse {

  private final Wish wish;
  private final List<Comment> comments;
  private final UserVotes userVotes;

  public WishResponse(Wish wish, List<Comment> comments, UserVotes userVotes) {
    this.wish = wish;
    this.comments = comments;
    this.userVotes = userVotes;
  }

  public Wish getWish() {
    return wish;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public UserVotes getUserVotes() {
    return userVotes;
  }

}
