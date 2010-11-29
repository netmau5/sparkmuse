package net.sparkmuse.data.play;

import net.sparkmuse.data.Cacheable;
import net.sparkmuse.data.Cache;
import net.sparkmuse.common.CacheKey;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import play.test.UnitTest;
import play.cache.EhCacheImpl;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public class PlayCacheTest extends UnitTest {

  private Cache newCache(){
    return new PlayCache(EhCacheImpl.getInstance());
  }

  @Test
  public void shouldAllowRawPutAndGet() {
    final Cache cache = newCache();
    Object obj1 = new SerializableObj();
    cache.put("Key", obj1);
    MatcherAssert.assertThat(cache.get("Key"), Matchers.is(obj1));
  }

  @Test
  public void shouldOverwriteValueOnRawPut() {
    final Cache cache = newCache();
    Object obj1 = new SerializableObj();
    Object obj2 = new SerializableObj();
    cache.put("Key", obj1);
    cache.put("Key", obj2);
    MatcherAssert.assertThat(cache.get("Key"), Matchers.is(obj2));
  }

  @Test(expected=NullPointerException.class)
  public void shouldNotAllowGetWithNullString() {
    final Cache cache = newCache();
    final String s = null;
    cache.get(s);
  }

  @Test(expected=NullPointerException.class)
  public void shouldNotAllowPutWithNullKey() {
    final Cache cache = newCache();
    cache.put(null, new SerializableObj());
  }

  @Test(expected=NullPointerException.class)
  public void shouldNotAllowPutWithNullValue() {
    final Cache cache = newCache();
    cache.put("key", null);
  }

  @Test(expected=IllegalArgumentException.class)
  public void shouldNotAllowPutWithNonSerializableValue(){
    final Cache cache = newCache();
    Object obj1 = new Object();
    cache.put("Key", obj1);
  }

  @Test
  public void shouldAllowCacheablePutAndGet() {
    final Cache cache = newCache();
    final CacheableObj cacheable = new CacheableObj();
    cache.put(cacheable);
    MatcherAssert.assertThat(cache.get(cacheable.getKey()), Matchers.is(cacheable.getInstance()));
  }

  @Test(expected=NullPointerException.class)
  public void shouldNotAllowCacheableGetWithNullCacheKey() {
    final Cache cache = newCache();
    CacheKey key = null;
    cache.get(key);
  }

  @Test(expected=NullPointerException.class)
  public void shouldNotAllowCacheablePutWithNullCacheable() {
    final Cache cache = newCache();
    cache.put(null);
  }

  @Test(expected=NullPointerException.class)
  public void shouldNotAllowCacheablePutWithNullCacheableCacheKey() {
    final Cache cache = newCache();
    final Cacheable cacheable = new Cacheable() {
      public CacheKey getKey() {
        return null;
      }

      public Object getInstance() {
        return new Object();
      }
    };
    cache.put(cacheable);
  }

  @Test(expected=NullPointerException.class)
  public void shouldNotAllowCacheablePutWithNullCacheableInstance() {
    final Cache cache = newCache();
    final Cacheable cacheable = new Cacheable() {
      public CacheKey getKey() {
        return new CacheKey<Object>(Object.class);
      }

      public Object getInstance() {
        return null;
      }
    };
    cache.put(cacheable);
  }

  @Test(expected=IllegalArgumentException.class)
  public void shouldNotAllowCacheablePutWithNonSerializableValue(){
    final Cache cache = newCache();
    final Cacheable<Object> cacheable = new Cacheable() {
      public CacheKey getKey() {
        return new CacheKey<Object>(Object.class);
      }

      public Object getInstance() {
        return new Object();
      }
    };
    cache.put(cacheable);
  }

  private static class SerializableObj implements Serializable {}

  private static class CacheableObj implements Cacheable<String>{
      public CacheKey<String> getKey() {
        return new CacheKey(Cacheable.class);
      }

      public String getInstance() {
        return "Hello World!";
      }
    };

}
