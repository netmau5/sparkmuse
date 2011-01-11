package net.sparkmuse.user;

/**
 * Signifies that an AccessToken is no longer valid, the user should
 * relogin to Twitter.
 *
 * @author neteller
 * @created: Jan 10, 2011
 */
public class TwitterLoginExpiredException extends RuntimeException {

  public TwitterLoginExpiredException() {
    super("Twitter login has expired.");
  }
}
