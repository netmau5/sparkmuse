package net.sparkmuse.common;

import net.sparkmuse.data.Cacheable;

import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.cache.CacheException;
import java.util.Collections;
import java.io.*;

import com.google.common.base.Preconditions;
import play.Logger;
import play.Play;
import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Jan 20, 2011
 */
public class JCache implements Cache {

  private javax.cache.Cache cache;
  private static final String VERSION = System.getenv("CURRENT_VERSION_ID");

  public JCache() {
    try {
      CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
      this.cache = cacheFactory.createCache(Collections.emptyMap());
      CacheManager.getInstance().registerCache("cache", this.cache);
    } catch (CacheException e) {
      Logger.error(e, "Error innitializing cache.");
    }
  }

  private javax.cache.Cache impl() {
    return cache;
  }

  //======

  public <T> T add(Cacheable<T> cacheable) {
    validate(cacheable);

    return add(cacheable.getKey().toString(), cacheable).getInstance();
  }

  public <T> T add(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);

    impl().put(versionKey(key), wrap(value));
    return value;
  }

  public <T> T safeAdd(Cacheable<T> cacheable) {
    validate(cacheable);

    return safeAdd(cacheable.getKey().toString(), cacheable).getInstance();
  }

  public <T> T safeAdd(String key, T value) {
    return add(key, value);
  }

  //======

  public <T> T get(String key, Class<T> clazz) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(clazz);

//    Logger.info("Key [" + key + "], Type [" + clazz + "]");

    return clazz.cast(get(key));
  }

  public Object get(String key) {
    Preconditions.checkNotNull(key);

    return unwrap(impl().get(versionKey(key)));
  }

  public <T> T get(final CacheKey<T> key) {
    Preconditions.checkNotNull(key);

    final Object o = unwrap(impl().get(versionKey(key.toString())));
    if (null != o) {
      return key.getImplementingClass().cast(o);
    }
    else {
//      Logger.debug("Cache miss [" + key.toString() + "], got [" + o + "]");
      return null;
    }
  }

  //======

  public <T> T set(String key, T value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    checkSerializable(value);

    impl().put(versionKey(key), wrap(value));
    return value;
  }

  public <T> T set(final Cacheable<T> cacheable) {
    validate(cacheable);

    impl().put(versionKey(cacheable.getKey().toString()), wrap(cacheable.getInstance()));
    return cacheable.getInstance();
  }

  public <T> T safeSet(String key, T value) {
    return set(key, value);
  }

  public <T> T safeSet(Cacheable<T> cacheable) {
    return set(cacheable);
  }

  //======

  public <T> T delete(Cacheable<T> cacheable) {
    validate(cacheable);

    delete(cacheable.getKey());
    return cacheable.getInstance();
  }

  public void delete(String key) {
    Preconditions.checkNotNull(key);

    impl().remove(versionKey(key));
  }

  public void delete(CacheKey key) {
    Preconditions.checkNotNull(key);

    delete(key.toString());
  }

  public <T> T safeDelete(Cacheable<T> cacheable) {
    validate(cacheable);

    delete(cacheable.getKey().toString());
    return cacheable.getInstance();
  }

  public void safeDelete(String key) {
    Preconditions.checkNotNull(key);

    delete(key);
  }

  public void safeDelete(CacheKey key) {
    Preconditions.checkNotNull(key);

    safeDelete(key.toString());
  }

  //======

  public <T> T replace(Cacheable<T> cacheable) {
    return set(cacheable);
  }

  public <T> T replace(String key, T value) {
    return set(key, value);
  }

  public <T> T safeReplace(Cacheable<T> cacheable) {
    return set(cacheable);
  }

  public <T> T safeReplace(String key, T value) {
    return set(key, value);
  }

  //======

  public void incr(String key) {
    throw new UnsupportedOperationException();
  }

  public void decr(String key) {
    throw new UnsupportedOperationException();
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

  byte[] wrap(Object o) {
        if(o == null) {
            return null;
        }
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bytes);
            oos.writeObject(o);
            return bytes.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Cannot cache a non-serializable value of type " + o.getClass().getName(), e);
        }
    }

  Object unwrap(Object bytes) {
      if(bytes == null) {
          return null;
      }
      try {
          return new ObjectInputStream(new ByteArrayInputStream((byte[])bytes)) {
              @Override
              protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                  return Class.forName(desc.getName(), false, Play.classloader);
              }
          }.readObject();
      } catch (Exception e) {
          Logger.error(e, "Error while deserializing cached value");
          return null;
      }
  }

  String versionKey(String key) {
    if (StringUtils.isEmpty(VERSION)) Logger.error("No version found.");
    return VERSION + "|" + key;
  }

}
