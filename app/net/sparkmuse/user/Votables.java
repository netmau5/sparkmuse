package net.sparkmuse.user;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public class Votables {
  public static String newKey(Votable votable) {
    return votable.getClass().getName() + "|" + votable.getId();
  }
}
