package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Embedded;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;

import java.util.Set;

import net.sparkmuse.common.Dateable;

/**
 * @author neteller
 * @created: Feb 10, 2011
 */
public class Activity extends Entity<Activity> implements Dateable {

  public enum Population {
    EVERYONE,
    USER
  }

  public enum Kind {
    SPARK,
    POST
  }

  public enum Source {
    FOLLOWER,
    REPLY
  }

  //define composite key, should be indexed
  private Kind kind;
  private Long contentKey;
  private Population type;
  private Long userId; //if Population.USER

  private Set<Source> sources;

  @Embedded
  private ItemSummary summary;

  private DateTime created;

  public Activity() {
    this.created = new DateTime();
    this.sources = Sets.newHashSet();
  }

  /**
   * A new post, visible as general activity to everyone.
   *
   * @param spark     Spark posted to
   * @param newPost   Post to the spark
   * @return
   */
  public static Activity newPostActivity(SparkVO spark, Post newPost) {
    Activity activity = new Activity();

    activity.kind = Kind.POST;
    activity.contentKey = newPost.getId();
    activity.type = Population.EVERYONE;

    activity.summary = new ItemSummary(spark, newPost.getAuthor());

    return activity;
  }

  /**
   * Someone posted to your Spark.
   *
   * @param spark     Spark posted to
   * @param newPost   Post to the spark
   * @return
   */
  public static Activity newSparkPostActivity(SparkVO spark, Post newPost) {
    Activity activity = new Activity();

    activity.kind = Kind.POST;
    activity.contentKey = newPost.getId();
    activity.type = Population.USER;
    activity.userId = spark.getAuthor().getId();

    activity.sources = Sets.newHashSet(Source.REPLY);
    activity.summary = new ItemSummary(spark, newPost.getAuthor());

    return activity;
  }

  /**
   * Someone replied to your post.
   *
   * @param spark     Spark posted to
   * @param inReplyTo Post that has been replied to
   * @param newPost   Replying post
   * @return
   */
  public static Activity newReplyPostActivity(SparkVO spark, Post inReplyTo, Post newPost) {
    Activity activity = new Activity();

    activity.kind = Kind.POST;
    activity.contentKey = newPost.getId();
    activity.type = Population.USER;
    activity.userId = inReplyTo.getAuthor().getId();
    
    activity.sources = Sets.newHashSet(Source.REPLY);
    activity.summary = new ItemSummary(spark, newPost.getAuthor());

    return activity;
  }

  /**
   * Someone has replied to a post you replied to,
   *
   * @param spark       Spark posted to
   * @param siblingPost Post that has been replied to (sibling)
   * @param newPost     Replying post
   * @return
   */
  public static Activity newSiblingReplyPostActivity(SparkVO spark, Post siblingPost, Post newPost) {
    return newReplyPostActivity(spark, siblingPost, newPost);
  }

  public Kind getKind() {
    return kind;
  }

  public void setKind(Kind kind) {
    this.kind = kind;
  }

  public Long getContentKey() {
    return contentKey;
  }

  public void setContentKey(Long contentKey) {
    this.contentKey = contentKey;
  }

  public Population getType() {
    return type;
  }

  public void setType(Population type) {
    this.type = type;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Set<Source> getSources() {
    return sources;
  }

  public void setSources(Set<Source> sources) {
    this.sources = sources;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public ItemSummary getSummary() {
    return summary;
  }

  public void setSummary(ItemSummary summary) {
    this.summary = summary;
  }

  public static class ItemSummary {

    private String userName; //twitter username
    private String sparkTitle;
    private Long sparkId;

    public ItemSummary() {
    }

    public ItemSummary(SparkVO spark, UserVO updateAuthor) {
      this.userName = updateAuthor.getUserName();
      this.sparkTitle = spark.getTitle();
      this.sparkId = spark.getId();
    }

    public String getUserName() {
      return userName;
    }

    public void setUserName(String userName) {
      this.userName = userName;
    }

    public String getSparkTitle() {
      return sparkTitle;
    }

    public void setSparkTitle(String sparkTitle) {
      this.sparkTitle = sparkTitle;
    }

    public Long getSparkId() {
      return sparkId;
    }

    public void setSparkId(Long sparkId) {
      this.sparkId = sparkId;
    }

  }

}
