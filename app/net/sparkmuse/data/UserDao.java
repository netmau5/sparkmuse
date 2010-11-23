package net.sparkmuse.data;

import net.sparkmuse.data.entity.UserVO;

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
  void vote(Votable voteable, UserVO voter);
}
