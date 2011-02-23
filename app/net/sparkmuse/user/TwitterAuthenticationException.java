package net.sparkmuse.user;

/**
 * Signifies that an AccessToken is no longer valid, the user should
 * relogin to Twitter.
 *
 * @author neteller
 * @created: Jan 10, 2011
 */
public class TwitterAuthenticationException extends RuntimeException {

  private TwitterAuthenticationException(String message) {
    super(message);
  }


  public static TwitterAuthenticationException accessDenied() {
    return new TwitterAuthenticationException("Twitter login has expired.");
  }
}
