package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Embedded;
import com.google.common.collect.Sets;
import com.google.common.base.Predicate;
import org.joda.time.DateTime;

import java.util.Set;
import java.io.Serializable;

import net.sparkmuse.common.Dateable;

/**
 * @author neteller
 * @created: Feb 10, 2011
 */
public class Activity extends Entity<Activity>
    implements Dateable, Comparable<Activity> {

  public enum Population {
    EVERYONE,
    USER
  }

  public enum Kind {
    SPARK,
    POST
  }

  public enum Source {
//    FOLLOWER,
    REPLY,
    LIKE,
    PERSONAL
  }

  //define composite key, should be indexed
  private Kind kind;
  private Long contentKey;
  private Population population;
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
   * @return
   */
  public static Activity newSparkActivity(SparkVO spark) {
    Activity activity = new Activity();

    activity.kind = Kind.SPARK;
    activity.contentKey = spark.getId();
    activity.population = Population.EVERYONE;

    activity.summary = new ItemSummary(spark, spark.getAuthor(), "");
    activity.created = spark.getCreated();

    return activity;
  }

  /**
   * A Spark liked by a user
   *
   * @param spark     Spark posted to
   * @return
   */
  public static Activity newSparkVoteActivity(SparkVO spark, UserVote userVote) {
    Activity activity = new Activity();

    activity.kind = Kind.SPARK;
    activity.contentKey = spark.getId();
    activity.population = Population.USER;
    activity.userId = userVote.authorUserId;

    activity.sources = Sets.newHashSet(Source.LIKE);
    activity.summary = new ItemSummary(spark, spark.getAuthor(), "You liked this Spark");
    activity.created = spark.getCreated();

    return activity;
  }

  /**
   * A new post, visible as general activity to everyone.
   *
   * @param spark     Spark posted to
   * @return
   */
  public static Activity newUserSparkActivity(SparkVO spark) {
    Activity activity = new Activity();

    activity.kind = Kind.SPARK;
    activity.contentKey = spark.getId();
    activity.population = Population.USER;
    activity.userId = spark.getAuthor().getId();

    activity.sources = Sets.newHashSet(Source.PERSONAL);
    activity.summary = new ItemSummary(spark, spark.getAuthor(), "");
    activity.created = spark.getCreated();

    return activity;
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
    activity.population = Population.EVERYONE;

    activity.summary = new ItemSummary(spark, newPost.getAuthor(), "");
    activity.created = newPost.getCreated();

    return activity;
  }

  /**
   * A post liked by a user.
   *
   * @param spark     Spark posted to
   * @param newPost   Post to the spark
   * @return
   */
  public static Activity newPostVoteActivity(SparkVO spark, Post newPost, UserVote userVote) {
    Activity activity = new Activity();

    activity.kind = Kind.POST;
    activity.contentKey = newPost.getId();
    activity.population = Population.USER;
    activity.userId = userVote.authorUserId;

    activity.sources = Sets.newHashSet(Source.LIKE);
    activity.summary = new ItemSummary(spark, newPost.getAuthor(), "You liked this post");
    activity.created = newPost.getCreated();

    return activity;
  }

  /**
   * A new post created by a user, should show as personal activity (me).
   *
   * @param spark     Spark posted to
   * @param newPost   Post to the spark
   * @return
   */
  public static Activity newUserPostActivity(SparkVO spark, Post newPost) {
    Activity activity = new Activity();

    activity.kind = Kind.POST;
    activity.contentKey = newPost.getId();
    activity.population = Population.USER;
    activity.userId = newPost.getAuthor().getId();

    activity.sources = Sets.newHashSet(Source.PERSONAL);
    activity.summary = new ItemSummary(spark, newPost.getAuthor(), "");
    activity.created = newPost.getCreated();

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
    activity.population = Population.USER;
    activity.userId = spark.getAuthor().getId();

    activity.sources = Sets.newHashSet(Source.REPLY);
    activity.summary = new ItemSummary(spark, newPost.getAuthor(), "Reply to your Spark");
    activity.created = newPost.getCreated();

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
    activity.population = Population.USER;
    activity.userId = inReplyTo.getAuthor().getId();
    
    activity.sources = Sets.newHashSet(Source.REPLY);
    activity.summary = new ItemSummary(spark, newPost.getAuthor(), "Reply to your post");
    activity.created = newPost.getCreated();

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

  public boolean isSpark() {
    return this.kind == Kind.SPARK;
  }

  public boolean isPost() {
    return this.kind == Kind.POST;
  }

  public boolean isPersonal() {
    return this.sources.contains(Source.PERSONAL);
  }

  public boolean isReply() {
    return this.sources.contains(Source.REPLY);
  }

  public boolean isLike() {
    return this.sources.contains(Source.LIKE);
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

  public Population getPopulation() {
    return population;
  }

  public void setPopulation(Population population) {
    this.population = population;
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

  public int compareTo(Activity activity) {
    if (activity.kind == kind && activity.contentKey.equals(contentKey)) return 0;
    else return activity.created.compareTo(created);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Activity)) return false;
    return 0 == ((Activity) o).compareTo(this);
  }

  public static class ItemSummary implements Serializable {

    private String userName; //twitter username
    private UserVO updateAuthor; //prefer to user id as twitter username can be changed :(
    private String sparkTitle;
    private Long sparkId;
    private String note;

    public ItemSummary() {
    }

    public ItemSummary(SparkVO spark, UserVO updateAuthor, String note) {
      this.userName = updateAuthor.getUserName();
      this.updateAuthor = updateAuthor;
      this.sparkTitle = spark.getTitle();
      this.sparkId = spark.getId();
      this.note = note;
    }

    public UserVO getUpdateAuthor() {
      return updateAuthor;
    }

    public void setUpdateAuthor(UserVO updateAuthor) {
      this.updateAuthor = updateAuthor;
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

    public String getNote() {
      return note;
    }

    public void setNote(String note) {
      this.note = note;
    }

    @Override
    public String toString() {
      return "[" + this.getSparkTitle() + ", " + this.getUserName() + "]";
    }
  }

  public static Predicate<Activity> isKind(final Kind kind) {
    return new Predicate<Activity>(){
      public boolean apply(Activity activity) {
        return activity.kind == kind;
      }
    };
  }

  @Override
  public String toString() {
    return "[" + this.getPopulation() + ", " + this.getKind() + "(" + this.getContentKey() + "), User(" + this.getUserId() + "), " + this.getSummary() + "]";
  }
}
