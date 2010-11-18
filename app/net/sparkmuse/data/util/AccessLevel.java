package net.sparkmuse.data.util;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public enum AccessLevel {
  UNAUTHORIZED,
  USER,
  ADMIN,
  DIETY;

  public boolean hasAuthorizationLevel(AccessLevel accessLevel) {
    return this.ordinal() >= accessLevel.ordinal();
  }
}
