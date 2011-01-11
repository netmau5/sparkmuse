package net.sparkmuse.user;

import com.google.inject.Inject;
import net.sparkmuse.common.Cache;
import net.sparkmuse.data.UserDao;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.data.entity.UserProfile;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 3, 2010
 */
public class UserFacade {

  private final UserDao userDao;
  private final Cache cache;
  private final TwitterService twitterService;

  @Inject
  public UserFacade(UserDao userDao, TwitterService twitterService, Cache cache) {
    this.userDao = userDao;
    this.cache = cache;
    this.twitterService = twitterService;
  }

  public String beginAuthentication() {
    return twitterService.beginAuthentication();
  }

  public UserVO registerAuthentication(String oauth_token, String oauth_verifier) throws InvalidOAuthRequestToken {
    UserVO user = userDao.findOrCreateUserBy(twitterService.registerAuthentication(oauth_token, oauth_verifier));
    cache.put(user);
    return user;
  }

  //@todo create token mechanism
  public boolean verifyAuthorizationToken(final UserVO user, final String authToken) {
    boolean validToken = "SM123".equals(authToken);
    if (validToken) {
      user.setAccessLevel(AccessLevel.USER);
      userDao.update(user);
      cache.put(user);
    }

    return validToken;
  }

  /**
   * Finds a user in the cache.  If not present, the db is queried and the cache is updated.
   *
   * @param id
   * @return
   */
  public UserVO findUserBy(final Long id) {
    return userDao.findUserBy(id);
  }

  public UserProfile getUserProfile(String userName) {
    return userDao.findUserProfileBy(userName);
  }

  public void applyForInvitation(final String userName, final String url) {
    userDao.saveApplication(userName, url);
  }

  public void recordUpVote(final Votable votable, final Long userId) {
    userDao.vote(votable, findUserBy(userId));
  }

  public void recordUpVote(String className, Long id, UserVO voter) {
    try {
      final Class clazz = Class.forName(className);
      if (Entity.class.isAssignableFrom(clazz)) {
        userDao.vote((Class<Entity>) clazz, id, voter);
      }
    } catch (ClassNotFoundException e) {
      return;
    }
  }

  public UserVotes findUserVotesFor(Set<Votable> votables, UserVO user) {
    final Set<UserVote> userVotes = userDao.findVotesFor(votables, user);
    return new UserVotes(userVotes);
  }
}
