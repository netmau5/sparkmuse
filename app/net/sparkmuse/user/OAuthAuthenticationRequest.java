package net.sparkmuse.user;

import twitter4j.http.RequestToken;

import java.io.Serializable;

/**
 * @author neteller
 * @created: Jan 16, 2011
 */
public class OAuthAuthenticationRequest implements Serializable {

  //oauth request token
  private String token;
  private String tokenSecret;

  private String authorizationUrl;

  public OAuthAuthenticationRequest(RequestToken token) {
    this.token = token.getToken();
    this.tokenSecret = token.getTokenSecret();
    this.authorizationUrl = token.getAuthorizationURL();
  }

  public String getToken() {
    return token;
  }

  public String getTokenSecret() {
    return tokenSecret;
  }

  public String getAuthorizationUrl() {
    return authorizationUrl;
  }

  @Override
  public String toString() {
    return "OAuthAuthenticationRequest{" +
        "token='" + token + '\'' +
        ", authorizationUrl='" + authorizationUrl + '\'' +
        '}';
  }
}
