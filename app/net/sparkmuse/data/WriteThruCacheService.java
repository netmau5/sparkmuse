package net.sparkmuse.data;

import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public interface WriteThruCacheService {

  /**
   * Cannot cache a non-inserted entity, these do not have a unique pk yet.
   *
   * @param cacheable
   * @param <T>
   * @return
   */
  <T extends Cacheable<T>> T put(Cacheable<T> cacheable);

  /**
   * Can only perform update datastore saves, new inserts should be
   * performed before a call.
   *
   * @param cacheable
   * @throws IllegalArgumentException when cacheable represents an entity
   * that has yet to be inserted
   * @return
   */
  <T extends Cacheable<T>> T putAndWrite(Cacheable<T> cacheable);
  <T extends Cacheable<T>> T get(CacheKey<T> key);
  Object get(String key);
}
