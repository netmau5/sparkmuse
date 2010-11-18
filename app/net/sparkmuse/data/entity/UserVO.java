package net.sparkmuse.data.entity;

import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.mapper.Model;
import net.sparkmuse.data.mapper.Property;
import net.sparkmuse.common.CacheKey;
import models.UserModel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
@Model(UserModel.class)
public class UserVO extends Entity<UserVO> {

  @Property("userName") private String userName;
  @Property("userId") private String authProviderUserId;
  @Property("authorization") private AccessLevel accessLevel;
  @Property("reputation") private int reputation;

  public boolean isAuthorizedFor(final AccessLevel accessLevel) {
    return this.accessLevel.hasAuthorizationLevel(accessLevel);
  }

  public static UserVO newUser(final String authProviderUserId, final String userName) {
    UserVO user = new UserVO();
    user.setUserName(userName);
    user.setAuthProviderUserId(authProviderUserId);
    user.setAccessLevel(AccessLevel.UNAUTHORIZED);
    user.setReputation(0);

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
}
