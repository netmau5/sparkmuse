package net.sparkmuse.user;

import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.user.Votable;

import java.util.Set;
import java.util.Map;

import com.google.common.collect.Maps;

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

}
