package net.sparkmuse.user;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.common.base.Preconditions;
import net.sparkmuse.common.Constants;
import net.sparkmuse.common.Cache;
import net.sparkmuse.data.entity.UserVO;
import twitter4j.TwitterFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.RequestToken;
import twitter4j.http.AccessToken;
import play.Logger;

/**
 * @author neteller
 * @created: Jan 10, 2011
 */
public class TwitterService {

  private static final String CONSUMER_KEY = "wZidO63kKxdvpcCgBtQGQ";
  private static final String CONSUMER_SECRET = "HH7POuDB0HPFKdbpabJhvI4QrBS1Zh7JeE10Fvy3Nc";

  @Inject
  @Named(Constants.TWITTER_CALLBACK_URI)
  private String CALLBACK_URI;

  private final Cache cache;
  private final TwitterFactory twitterFactory;

  @Inject
  public TwitterService(Cache cache) {
    this.twitterFactory = new TwitterFactory();
    this.cache = cache;
  }

  private Twitter newTwitterInstance() {
    final Twitter twitter = twitterFactory.getInstance();
    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    return twitter;
  }

  private Twitter newUserTwitterInstance(UserVO user) {
    final Twitter twitter = twitterFactory.getInstance(user.newAccessToken());
    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    return twitter;
  }

  /**
   * Begins a Twitter OAuth authentication request.
   *
   * @return authentication endpoint url
   */
  public String beginAuthentication() {
    Twitter twitter = newTwitterInstance();

    RequestToken rtoken = null;
    try {
      rtoken = twitter.getOAuthRequestToken(CALLBACK_URI);
    } catch (TwitterException e) {
      throw new RuntimeException(e);
    }

    cache.put(rtoken.getToken(), rtoken);

    return rtoken.getAuthorizationURL();
  }

  public UserLogin registerAuthentication(String oauth_token, String oauth_verifier) throws InvalidOAuthRequestToken {
    Preconditions.checkNotNull(oauth_token);
    Preconditions.checkNotNull(oauth_verifier);
    Logger.info("Twitter authentication received: " + oauth_token + " : " + oauth_verifier);

    Twitter twitter = newTwitterInstance();
    RequestToken requestToken = cache.get(oauth_token, RequestToken.class);
    if (null == requestToken) throw new InvalidOAuthRequestToken();

    try {
      AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
      return new UserLogin(twitter, accessToken);
    } catch (TwitterException e) {
      if (e.getStatusCode() == 401) {
        Logger.error(e, "Twitter denied access.");
        return null;
      }
      Logger.error(e, "Error connecting with Twitter.");
      throw new RuntimeException(e);
    }
  }


}
