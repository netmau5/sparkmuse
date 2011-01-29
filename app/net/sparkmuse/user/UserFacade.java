package net.sparkmuse.user;

import com.google.inject.Inject;
import net.sparkmuse.common.Cache;
import net.sparkmuse.data.UserDao;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.ajax.InvalidRequestException;

import java.util.Set;
import java.util.List;

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

  public OAuthAuthenticationRequest beginAuthentication() {
    return twitterService.beginAuthentication();
  }

  public UserVO registerAuthentication(OAuthAuthenticationResponse response) {
    UserVO user = userDao.findOrCreateUserBy(twitterService.registerAuthentication(response));
    cache.set(user);
    return user;
  }

  public List<UserProfile> getAllProfiles() {
    return userDao.getAllProfiles();
  }

  public UserProfile createUser(String userName) {
    final UserProfile userProfile = getUserProfile(userName);
    return null == userProfile ? userDao.createUser(userName) : userProfile;
  }

  public void updateUser(long userId, AccessLevel accessLevel, int invites) {
    final UserVO user = findUserBy(userId);
    user.setAccessLevel(accessLevel);
    userDao.store(user);
    final UserProfile userProfile = getUserProfile(user.getUserName());
    userProfile.setInvites(invites);
    userDao.store(userProfile);

    cache.delete(user.getKey()); //@todo do this globally in Afters?
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

  public UserProfile updateProfile(UserProfile profile) {
    return userDao.update(profile);
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

  public void recordNewSpark(UserVO user) {
    user.setSparks(user.getSparks() + 1);
    userDao.store(user);
  }

  public void recordNewPost(UserVO user) {
    user.setPosts(user.getPosts() + 1);
    userDao.store(user);
  }

  public UserVotes findUserVotesFor(Set<Votable> votables, UserVO user) {
    final Set<UserVote> userVotes = userDao.findVotesFor(votables, user);
    return new UserVotes(userVotes);
  }

  public int inviteFriend(UserVO inviter, String friend) {
    final UserProfile inviterProfile = getUserProfile(inviter.getUserName());
    if (inviterProfile.getInvites() > 0) {

      final UserProfile newUserProfile = createUser(friend.startsWith("@") ? friend.substring(1) : friend);
      final UserVO newUser = newUserProfile.getUser();

      if (newUser.getAccessLevel().hasAuthorizationLevel(AccessLevel.USER)) {
        throw new InvalidRequestException("The user you invited is already a member, save that invite!");
      }

      newUser.setAccessLevel(AccessLevel.USER);
      userDao.store(newUser);

      final int remainingInvites = inviterProfile.getInvites() - 1;
      inviterProfile.setInvites(remainingInvites);
      updateProfile(inviterProfile);

      return remainingInvites;
    }

    return 0;
  }

  public void tweet(UserVO from, String message) {
    twitterService.tweet(from, message);
  }
}
