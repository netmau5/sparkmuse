package net.sparkmuse.data;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.VoteVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface UserDao {
  UserVO findOrCreateUserBy(String authProviderUserId, String userName);
  UserVO findUserBy(Long id);
  UserVO update(UserVO user);
  void saveApplication(String userName, String url);
  VoteVO saveVote(VoteVO vote);
}
