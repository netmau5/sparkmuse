package net.sparkmuse.data;

import net.sparkmuse.common.CacheKey;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface Cacheable<T> {

  CacheKey<T> getKey();

  T getInstance();

}
