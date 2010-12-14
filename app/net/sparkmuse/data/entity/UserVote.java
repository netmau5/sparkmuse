package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Id;
import net.sparkmuse.user.Votable;
import net.sparkmuse.user.Votables;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public class UserVote {

  @Id
  private String key; //should be [entityClassName]|[entityId]
  private int voteWeight; //should be -1, 0, +1

  public String entityClassName;
  public Long entityId;
  public Long authorUserId;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public int getVoteWeight() {
    return voteWeight;
  }

  public void setVoteWeight(int voteWeight) {
    this.voteWeight = voteWeight;
  }

  public static UserVote newUpVote(Votable votable, UserVO voter) {
    final UserVote vm = new UserVote();
    vm.entityClassName = votable.getClass().getName();
    vm.entityId = votable.getId();
    vm.key = Votables.newKey(votable);
    vm.voteWeight = 1;
    vm.authorUserId = voter.getId();
    return vm;
  }

}
