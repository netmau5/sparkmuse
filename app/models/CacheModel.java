package models;

import com.vercer.engine.persist.annotation.Key;

/**
 * Represents a data-backed cache entry.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class CacheModel {

  @Key public Long id;
  public String key;
  public String value;

}
