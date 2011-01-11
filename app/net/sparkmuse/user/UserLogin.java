package net.sparkmuse.user;

import twitter4j.http.AccessToken;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * @author neteller
 * @created: Jan 10, 2011
 */
public class UserLogin {

  private Twitter twitter;
  private AccessToken accessToken;

  public UserLogin(Twitter twitter, AccessToken accessToken) {
    this.twitter = twitter;
    this.accessToken = accessToken;
  }

  public String getAuthProviderUserId() {
    return Integer.toString(accessToken.getUserId());
  }

  public String getScreenName() {
    try {
      return twitter.getScreenName();
    } catch (TwitterException e) {
      throw new RuntimeException(e);
    }
  }

  public String getToken() {
    return accessToken.getToken();
  }

  public String getTokenSecret() {
    return accessToken.getTokenSecret();
  }

}
