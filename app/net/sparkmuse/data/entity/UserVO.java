package net.sparkmuse.data.entity;

import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.NullTo;
import net.sparkmuse.user.UserLogin;
import com.google.common.base.Function;
import org.joda.time.DateTime;
import org.joda.time.Days;
import twitter4j.http.AccessToken;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class UserVO extends Entity<UserVO> {

  private static final long serialVersionUID = 1L; 

  private String userName;
  private String userNameLowercase; //used for queries on username
  private String authProviderUserId;
  private AccessLevel accessLevel;

  private DateTime firstLogin;
  private DateTime lastLogin;

  //oauth stuff, used to recreate AccessToken
  private String oauthToken;
  private String oauthTokenSecret;

  //statistics
  private int reputation;
  private int sparks;
  private int posts;

  public boolean isAuthorizedFor(final AccessLevel accessLevel) {
    return this.accessLevel.hasAuthorizationLevel(accessLevel);
  }

  public boolean isAdmin() {
    return isAuthorizedFor(AccessLevel.ADMIN);
  }

  public boolean isUser() {
    return isAuthorizedFor(AccessLevel.USER);
  }

  public boolean isUnauthorized() {
    return this.accessLevel == AccessLevel.UNAUTHORIZED;
  }

  public boolean isNewUser() {
    return Days.daysBetween(NullTo.now(firstLogin), new DateTime()).getDays() <= 1;
  }


  public static UserVO newUser(String userName) {
    UserVO user = new UserVO();
    user.setUserName(userName);
    user.setUserNameLowercase(userName.toLowerCase());
    user.setAccessLevel(AccessLevel.UNAUTHORIZED);
    user.setReputation(0);

    if (userName.equalsIgnoreCase("Sparkmuse")) user.setAccessLevel(AccessLevel.DIETY);

    return user;
  }

  public UserVO updateUserDuring(final UserLogin login) {
    this.setAuthProviderUserId(login.getAuthProviderUserId());
    this.setLastLogin(new DateTime());
    this.setOauthToken(login.getToken());
    this.setOauthTokenSecret(login.getTokenSecret());

    if (null == firstLogin) this.setFirstLogin(new DateTime());

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

  public DateTime getFirstLogin() {
    return firstLogin;
  }

  public void setFirstLogin(DateTime firstLogin) {
    this.firstLogin = firstLogin;
  }

  public int getSparks() {
    return sparks;
  }

  public void setSparks(int sparks) {
    this.sparks = sparks;
  }

  public int getPosts() {
    return posts;
  }

  public void setPosts(int posts) {
    this.posts = posts;
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
