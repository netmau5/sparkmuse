package models;

import com.google.code.twig.annotation.Id;


/**
 * Represents a data-backed cache entry.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class CacheModel {

  @Id
  public Long id;
  public String key;
  public String value;

}
