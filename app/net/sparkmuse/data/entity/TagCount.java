package net.sparkmuse.data.entity;

import com.google.common.collect.Lists;
import com.google.code.twig.annotation.Id;

import java.util.List;
import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Mar 23, 2011
 */
public class TagCount implements Serializable {

  public static String NAME_WISH_TAG_COUNTER = "WISH";
  public static String CACKEKEY_WISH_TAG_COUNTER = "CacheWishTagCounter";

  @Id
  private String typeName;
  private List<String> topTags;
  private DateTime created;

  public TagCount() {
    this.created = new DateTime();
    this.topTags = Lists.newArrayList();
  }

  public TagCount(String typeName, List<String> topTags) {
    this();
    this.typeName = typeName;
    this.topTags = topTags;
  }

  public List<String> getTopTags() {
    return topTags;
  }

  public void setTopTags(List<String> topTags) {
    this.topTags = topTags;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }
  
}