package net.sparkmuse.user;

import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.user.Votable;

import java.util.Set;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public class UserVotes {

  private Map<String, Boolean> votableKeyToUpVoteMap;

  public UserVotes(Set<UserVote> userVotes) {
    votableKeyToUpVoteMap = Maps.newHashMap();
    for (UserVote userVote : userVotes) {
      votableKeyToUpVoteMap.put(userVote.getKey(), userVote.getVoteWeight() == 1);
    }
  }

  public boolean hasUpVoted(Votable votable) {
    final Boolean toReturn = votableKeyToUpVoteMap.get(Votables.newKey(votable));
    return null != toReturn ? toReturn : false;
  }

  /**
   * Instance denoting a single user vote over whatever cross-section this
   * data is being applied to.  This is typically relevant for new posts where
   * we only return the single post created.
   *
   * @param votable
   * @return
   */
  public static UserVotes only(Votable votable, UserVO voter) {
    return new UserVotes(Sets.newHashSet(UserVote.newUpVote(votable, voter)));
  }

}
