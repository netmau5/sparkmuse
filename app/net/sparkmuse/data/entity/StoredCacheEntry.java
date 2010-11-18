package net.sparkmuse.data.entity;

import net.sparkmuse.common.CacheKey;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class StoredCacheEntry<T> {

  private CacheKey key;
  private T value; //must be serializable!

  public StoredCacheEntry(CacheKey key, T value) {
    this.key = key;
    this.value = value;
  }

  public CacheKey getKey() {
    return key;
  }

  public void setKey(CacheKey key) {
    this.key = key;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }
}
