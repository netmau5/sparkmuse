package net.sparkmuse.data;

import net.sparkmuse.data.entity.StoredCacheEntry;
import net.sparkmuse.common.CacheKey;
import models.CacheModel;

/**
 * Read/Writes cache data to a persistent data store
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public interface CacheDao {

  public <T> StoredCacheEntry save(Cacheable<T> cacheable);
  public void save(String key, Object value);
  public <T> StoredCacheEntry<T> find(CacheKey<T> cacheKey);

}
