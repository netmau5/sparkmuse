package models;

import com.vercer.engine.persist.annotation.Key;

/**
 * A generic table for any attachment to a Spark.  This might
 * include images, resources, competitors, etc.
 *
 * @author neteller
 * @created: Nov 16, 2010
 */
public class AssetModel {

  @Key
  public long id;
  public Long postId; //post where this asset was attached
  public Long sparkId; //spark that is the owner of this asset
  public Long userId; //user who posted it

  //generic
  public Type type;
  public int votes;
  public String name;

  //resources & competitors
  public String url;

  //leading-question
  public String leadingQuestion;

  public enum Type {
    RESOURCE,
    COMPETITOR,
    LEADING_QUESTION
  }

}
