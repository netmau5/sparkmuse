package net.sparkmuse.user;

import twitter4j.http.RequestToken;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Jan 16, 2011
 */
public class OAuthAuthenticationResponse {

  private String token; //request token identifier
  private String verifier;

  private RequestToken requestToken;

  public OAuthAuthenticationResponse(RequestToken requestToken, String tokenString, String verifier) {
    Preconditions.checkNotNull(requestToken);
    Preconditions.checkNotNull(tokenString);
    Preconditions.checkNotNull(verifier);
    Preconditions.checkState(StringUtils.equals(tokenString, requestToken.getToken()));
    this.token = tokenString;
    this.verifier = verifier;
    this.requestToken = requestToken;
  }

  public String getToken() {
    return token;
  }

  public String getVerifier() {
    return verifier;
  }

  public RequestToken getRequestToken() {
    return requestToken;
  }

  @Override
  public String toString() {
    return "OAuthAuthenticationResponse{" +
        "token='" + token + '\'' +
        ", verifier='" + verifier + '\'' +
        '}';
  }

}
