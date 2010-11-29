package net.sparkmuse.data.entity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public class UserVote {

  private String key;
  private int voteWeight;

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

}
