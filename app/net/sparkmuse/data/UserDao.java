package net.sparkmuse.data;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.user.Votable;
import net.sparkmuse.user.UserLogin;

import java.util.Set;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface UserDao {
  UserVO findOrCreateUserBy(UserLogin login);

  /**
   * Finds a user in the cache.  If not present, the db is queried and the cache is updated.
   *
   * @param id
   * @return
   */
  UserVO findUserBy(Long id);

  UserProfile findUserProfileBy(String userName);

  Map<Long, UserVO> findUsersBy(Set<Long> ids);
  void saveApplication(String userName, String url);

  /**
   * Stores a record of the vote for the given user, upvotes the votable, stores
   * it to the datastore, and adjusts the author's reputation.
   *
   * @param votable
   * @param voter
   */
  void vote(Votable votable, UserVO voter);
  <T extends Entity<T>> void vote(Class<T> entityClass, Long id, UserVO voter);
  Set<UserVote> findVotesFor(Set<Votable> votables, UserVO user);

  <T extends Entity<T>> T update(T entity);
  <T extends Entity<T>> T store(T entity);
}
