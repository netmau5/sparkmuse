package net.sparkmuse.common;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class CacheKey<T> {

  private final Class<T> clazz;
  private final String key;

  /**
   *
   * @param clazz         Class name of entity
   * @param uniqueFields  Set of unique fields (creating a unique index) for an entity.
   *                      No unique fields are required for singletons.
   */
  public CacheKey(Class<T> clazz, Object... uniqueFields) {
    this.clazz = clazz;

    List keyParts = null == uniqueFields || uniqueFields.length == 0 ? Lists.newArrayList() :
        Lists.newArrayList(Arrays.asList(uniqueFields));
    keyParts.add(0, clazz.getName());
    this.key = Joiner.on("|").join(keyParts);
  }

  @Override
  public String toString() {
    return key;
  }

  public Class<T> getImplementingClass() {
    return clazz;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof CacheKey && ((CacheKey) o).key.equals(this.key);
  }
}
