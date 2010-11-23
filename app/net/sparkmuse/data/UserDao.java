package net.sparkmuse.data;

import net.sparkmuse.data.entity.UserVO;

import java.util.Set;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface UserDao {
  UserVO findOrCreateUserBy(String authProviderUserId, String userName);
  UserVO findUserBy(Long id);
  Map<Long, UserVO> findUsersBy(Set<Long> ids);
  UserVO update(UserVO user);
  void saveApplication(String userName, String url);
  void vote(Votable voteable, UserVO voter);
}
