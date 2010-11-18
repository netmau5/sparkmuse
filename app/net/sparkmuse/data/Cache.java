package net.sparkmuse.data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public interface Cache {

  void set(String key, Object o, String timeout);

  void add(String key, Object o, String timeout);

  <T> T get(String key, Class<T> clazz);

  Object get(String key);

}
