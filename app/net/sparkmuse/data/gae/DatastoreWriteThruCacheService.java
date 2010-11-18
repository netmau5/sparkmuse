package net.sparkmuse.data.gae;

import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.data.WriteThruCacheService;
import net.sparkmuse.data.CacheDao;
import net.sparkmuse.data.Cache;
import net.sparkmuse.data.entity.StoredCacheEntry;
import net.sparkmuse.task.IssueTaskService;
import com.google.inject.Inject;

/**
 * Wraps the GAE cache and backs data up in the datastore.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class DatastoreWriteThruCacheService implements WriteThruCacheService {

  private final IssueTaskService issueTaskService;
  private final CacheDao cacheDao;
  private final Cache cache;

  @Inject
  public DatastoreWriteThruCacheService(final IssueTaskService issueTaskService,
                                        final CacheDao cacheDao,
                                        final Cache cache) {
    this.issueTaskService = issueTaskService;
    this.cacheDao = cacheDao;
    this.cache = cache;
  }
  
  public <T extends Cacheable<T>> T put(final Cacheable<T> cacheable) {
    verifyUniqueKey(cacheable);
    cache.set(cacheable.getKey().toString(), cacheable, "30d");
    return cacheable.getInstance();
  }

  public <T extends Cacheable<T>> T putAndWrite(Cacheable<T> cacheable) {
    put(cacheable);
    issueTaskService.issueCachePersistTask(cacheable);
    return cacheable.getInstance();
  }

  private void verifyUniqueKey(Cacheable cacheable) {
    try {
      cacheable.getKey();
    }
    catch (Throwable t) {
      throw new IllegalArgumentException("Could not get a unique cache key for [" + cacheable.getClass() + "]");
    }
  }

  public <T extends Cacheable<T>> T get(final CacheKey<T> key) {
    Cacheable cacheable = cache.get(key.toString(), Cacheable.class);
    if (null == cacheable) cacheable = reloadFromDatastore(key);

    if (null != cacheable) return (T) cacheable.getInstance();
    else return null;
  }

  public Object get(final String key) {
    return cache.get(key);
  }

  private <T extends Cacheable<T>> T reloadFromDatastore(final CacheKey<T> key){
    final StoredCacheEntry<T> cacheEntry = cacheDao.find(key);
    if (null != cacheEntry) {
      cache.add(key.toString(), cacheEntry.getValue(), "30d");
      return cacheEntry.getValue();
    }
    else {
      return null;
    }
  }

}
