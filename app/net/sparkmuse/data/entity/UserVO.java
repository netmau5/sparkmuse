package net.sparkmuse.data.entity;

import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.user.UserLogin;
import com.google.common.base.Function;
import org.joda.time.DateTime;
import twitter4j.http.AccessToken;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class UserVO extends Entity<UserVO> {

  private String userName;
  private String userNameLowercase; //used for queries on username
  private String authProviderUserId;
  private AccessLevel accessLevel;
  private int reputation;

  private DateTime lastLogin;

  //oauth stuff, used to recreate AccessToken
  private String oauthToken;
  private String oauthTokenSecret;

  public boolean isAuthorizedFor(final AccessLevel accessLevel) {
    return this.accessLevel.hasAuthorizationLevel(accessLevel);
  }

  public boolean isAdmin() {
    return isAuthorizedFor(AccessLevel.ADMIN);
  }

  public static UserVO newUser(final UserLogin login) {
    String userName = login.getScreenName();

    UserVO user = new UserVO();
    user.setUserName(userName);
    user.setUserNameLowercase(userName.toLowerCase());
    user.setAuthProviderUserId(login.getAuthProviderUserId());
    user.setAccessLevel(AccessLevel.UNAUTHORIZED);
    user.setReputation(0);
    user.setLastLogin(new DateTime());
    user.setOauthToken(login.getToken());
    user.setOauthTokenSecret(login.getTokenSecret());

    if (userName.equalsIgnoreCase("Sparkmuse")) user.setAccessLevel(AccessLevel.DIETY);

    return user;
  }

  public UserVO updateUserDuring(final UserLogin login) {
    this.setOauthToken(login.getToken());
    this.setOauthTokenSecret(login.getTokenSecret());
    this.setLastLogin(new DateTime());
    return this;
  }

  public AccessToken newAccessToken() {
    return new AccessToken(getOauthToken(), getOauthTokenSecret());
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

  public String getUserNameLowercase() {
    return userNameLowercase;
  }

  public void setUserNameLowercase(String userNameLowercase) {
    this.userNameLowercase = userNameLowercase;
  }

  public static Function<UserVO, Long> getAsUserIds() {
    return asUserIds;
  }

  public static void setAsUserIds(Function<UserVO, Long> asUserIds) {
    UserVO.asUserIds = asUserIds;
  }

  public DateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(DateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public String getOauthToken() {
    return oauthToken;
  }

  public void setOauthToken(String oauthToken) {
    this.oauthToken = oauthToken;
  }

  public String getOauthTokenSecret() {
    return oauthTokenSecret;
  }

  public void setOauthTokenSecret(String oauthTokenSecret) {
    this.oauthTokenSecret = oauthTokenSecret;
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
