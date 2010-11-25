package net.sparkmuse.data;

import net.sparkmuse.common.CacheKey;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface Cacheable<T> extends Serializable {

  CacheKey<T> getKey();

  T getInstance();

}
