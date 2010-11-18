package net.sparkmuse.data.play;

import net.sparkmuse.data.Cache;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class PlayCache implements Cache {

  public void set(String key, Object o, String timeout) {
    play.cache.Cache.set(key, o, timeout);
  }

  public <T> T get(String key, Class<T> clazz) {
    return play.cache.Cache.get(key, clazz);
  }

  public void add(String key, Object o, String timeout) {
    play.cache.Cache.add(key, o, timeout);
  }

  public Object get(String key) {
    return play.cache.Cache.get(key);
  }
}
