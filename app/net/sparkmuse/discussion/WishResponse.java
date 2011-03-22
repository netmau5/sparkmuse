package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.Comment;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.entity.Commitment;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.common.CommitmentType;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.base.Function;

/**
 * @author neteller
 * @created: Mar 21, 2011
 */
public class WishResponse {

  private final Wish wish;
  private final List<Comment> comments;
  private final UserVotes userVotes;
  private final Map<CommitmentType, Commitment> commitments;

  public WishResponse(Wish wish, List<Comment> comments, UserVotes userVotes, List<Commitment> commitments) {
    this.wish = wish;
    this.comments = comments;
    this.userVotes = userVotes;
    this.commitments = Maps.uniqueIndex(commitments, new Function<Commitment, CommitmentType>(){
      public CommitmentType apply(Commitment commitment) {
        return commitment.getCommitmentType();
      }
    });
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

  public boolean hasCommittedToTry() {
    return null != this.commitments.get(CommitmentType.TRY);
  }

  public boolean hasCommittedToBuy() {
    return null != this.commitments.get(CommitmentType.BUY);
  }

  public boolean hasCommittedToSurvey() {
    return null != this.commitments.get(CommitmentType.SURVEY);
  }

  public boolean hasCommittedToSee() {
    return null != this.commitments.get(CommitmentType.SEE);
  }

}
