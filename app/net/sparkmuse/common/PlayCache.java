package net.sparkmuse.common;

import net.sparkmuse.common.Cache;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;
import com.google.common.base.Preconditions;

import java.io.Serializable;

import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import play.Logger;

/**
 * Stupid impl is because Play cache can only be accessed statically :(
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class PlayCache implements Cache {

  private static final int THIRTY_DAYS = 60 * 60 * 24 * 30;

  private final play.cache.CacheImpl cache;

  public PlayCache(play.cache.CacheImpl cache) {
    this.cache = cache;
  }

  public PlayCache(){
    this(null);
  }

  private play.cache.CacheImpl impl() {
    if (null != cache) {
      Logger.debug("Using logger 1: " + cache.getClass());
      return cache;
    }
    else if (null != play.cache.Cache.forcedCacheImpl) {
      Logger.debug("Using logger 1: " + play.cache.Cache.forcedCacheImpl);
      return play.cache.Cache.forcedCacheImpl;
    }
    else if (null != play.cache.Cache.cacheImpl) {
      Logger.debug("Using logger 1: " + play.cache.Cache.cacheImpl);
      return play.cache.Cache.cacheImpl;
    }

    throw new IllegalStateException("No cache implementation available.");
  }

  //======

  public <T> T add(Cacheable<T> cacheable) {
    validate(cacheable);

    return add(cacheable.getKey().toString(), cacheable).getInstance();
  }

  public <T> T add(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);

    impl().add(key, value, THIRTY_DAYS);
    return value;
  }

  public <T> T safeAdd(Cacheable<T> cacheable) {
    validate(cacheable);

    return safeAdd(cacheable.getKey().toString(), cacheable).getInstance();
  }

  public <T> T safeAdd(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);

    impl().safeAdd(key, value, THIRTY_DAYS);
    return value;
  }

  //======

  public <T> T get(String key, Class<T> clazz) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(clazz);

    return clazz.cast(get(key));
  }

  public Object get(String key) {
    Preconditions.checkNotNull(key);

    return impl().get(key);
  }

  public <T> T get(final CacheKey<T> key) {
    Preconditions.checkNotNull(key);

    Cacheable<T> cacheable = get(key.toString(), Cacheable.class);
    if (null != cacheable) return cacheable.getInstance();
    else return null;
  }

  //======

  public <T> T set(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    checkSerializable(value);

    impl().set(key, value, THIRTY_DAYS);
    return value;
  }

  public <T> T set(final Cacheable<T> cacheable) {
    validate(cacheable);

    set(cacheable.getKey().toString(), cacheable.getInstance());
    return cacheable.getInstance();
  }

  public <T> T safeSet(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    checkSerializable(value);

    impl().safeSet(key, value, THIRTY_DAYS);
    return value;
  }

  public <T> T safeSet(Cacheable<T> cacheable) {
    validate(cacheable);

    safeSet(cacheable.getKey().toString(), cacheable.getInstance());
    return cacheable.getInstance();
  }

  //======

  public <T> T delete(Cacheable<T> cacheable) {
    validate(cacheable);

    impl().delete(cacheable.getKey().toString());
    return cacheable.getInstance();
  }

  public void delete(String key) {
    Preconditions.checkNotNull(key);

    impl().delete(key);
  }

  public void delete(CacheKey key) {
    Preconditions.checkNotNull(key);

    delete(key.toString());
  }

  public <T> T safeDelete(Cacheable<T> cacheable) {
    validate(cacheable);

    impl().safeDelete(cacheable.getKey().toString());
    return cacheable.getInstance();
  }

  public void safeDelete(String key) {
    Preconditions.checkNotNull(key);

    impl().safeDelete(key);
  }

  public void safeDelete(CacheKey key) {
    Preconditions.checkNotNull(key);

    safeDelete(key.toString());
  }

  //======

  public <T> T replace(Cacheable<T> cacheable) {
    validate(cacheable);

    return replace(cacheable.getKey().toString(), cacheable).getInstance();
  }

  public <T> T replace(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    checkSerializable(value);

    impl().replace(key, value, THIRTY_DAYS);
    return value;
  }

  public <T> T safeReplace(Cacheable<T> cacheable) {
    validate(cacheable);

    return safeReplace(cacheable.getKey().toString(), cacheable).getInstance();
  }

  public <T> T safeReplace(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    checkSerializable(value);

    impl().safeReplace(key, value, THIRTY_DAYS);
    return value;
  }

  //======

  public void incr(String key) {
    Preconditions.checkNotNull(key);
    impl().incr(key, 1);
  }

  public void decr(String key) {
    Preconditions.checkNotNull(key);
    impl().decr(key, 1);
  }

  public void clear() {
    impl().clear();
  }

  //======  

  private static void checkSerializable(Object o) {
    if (!(o instanceof Serializable)) {
      throw new IllegalArgumentException(o + " is not serializable, cannot be cached.");
    }
  }

  private static <T> Cacheable<T> validate(Cacheable<T> cacheable) {
    Preconditions.checkNotNull(cacheable);
    Preconditions.checkNotNull(cacheable.getKey(), "Could not get a unique cache key for [" + cacheable.getClass() + "]");
    Preconditions.checkNotNull(cacheable.getInstance());
    checkSerializable(cacheable.getInstance());

    return cacheable;
  }
}
