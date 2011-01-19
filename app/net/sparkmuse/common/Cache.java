package net.sparkmuse.common;

import net.sparkmuse.common.CacheKey;
import net.sparkmuse.data.Cacheable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public interface Cache {

  <T> T add(Cacheable<T> cacheable);

  <T> T add(String key, T value);

  <T> T safeAdd(Cacheable<T> cacheable);

  <T> T safeAdd(String key, T value);

  //======


  <T> T get(CacheKey<T> key);

  Object get(String key);

  <T> T get(String key, Class<T> type);

  //======

  /**
   * Cannot cache a non-inserted entity, these do not have a unique pk yet.
   *
   * @param cacheable
   * @param <T>
   * @return
   */
  <T> T set(Cacheable<T> cacheable);

  <T> T set(String key, T value);

  <T> T safeSet(Cacheable<T> cacheable);

  <T> T safeSet(String key, T value);

  //======

  <T> T delete(Cacheable<T> cacheable);

  void delete(String key);

  void delete(CacheKey key);

  <T> T safeDelete(Cacheable<T> cacheable);

  void safeDelete(String key);

  void safeDelete(CacheKey key);

  //======

  <T> T replace(Cacheable<T> cacheable);

  <T> T replace(String key, T value);

  <T> T safeReplace(Cacheable<T> cacheable);

  <T> T safeReplace(String key, T value);

  //======

  void incr(String key);

  void decr(String key);

  void clear();

}
