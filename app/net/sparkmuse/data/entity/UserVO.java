package net.sparkmuse.data.entity;

import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.common.CacheKey;
import com.google.common.base.Function;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class UserVO extends Entity<UserVO> {

  private String userName;
  private String authProviderUserId;
  private AccessLevel accessLevel;
  private int reputation;

  public boolean isAuthorizedFor(final AccessLevel accessLevel) {
    return this.accessLevel.hasAuthorizationLevel(accessLevel);
  }

  public static UserVO newUser(final String authProviderUserId, final String userName) {
    UserVO user = new UserVO();
    user.setUserName(userName);
    user.setAuthProviderUserId(authProviderUserId);
    user.setAccessLevel(AccessLevel.UNAUTHORIZED);
    user.setReputation(0);

    if (userName.equalsIgnoreCase("Sparkmuse")) user.setAccessLevel(AccessLevel.DIETY);

    return user;
  }

  public static CacheKey cacheKeyFor(long userId) {
    return new CacheKey(UserVO.class, userId);
  }

  public String getAuthProviderUserId() {
    return authProviderUserId;
  }

  public void setAuthProviderUserId(String authProviderUserId) {
    this.authProviderUserId = authProviderUserId;
  }

  public AccessLevel getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(AccessLevel accessLevel) {
    this.accessLevel = accessLevel;
  }

  public int getReputation() {
    return reputation;
  }

  public void setReputation(int reputation) {
    this.reputation = reputation;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public String toString() {
    return userName;
  }

  public static Function<UserVO, Long> asUserIds = new Function<UserVO, Long>(){
    public Long apply(UserVO entity) {
      return entity.getId();
    }
  };
}
