package net.sparkmuse.data.entity;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Feb 20, 2011
 */
public class AppStat extends Entity<AppStat> {

  public enum Type {
    USERS,
    POSTS,
    SPARKS
  }

  private DateTime created;
  private Type type;
  private int count;

  public AppStat() {
    this.created = new DateTime();
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public static AppStat newUserCount(int count) {
    AppStat appStat = new AppStat();
    appStat.setType(Type.USERS);
    appStat.setCount(count);
    return appStat;
  }

  public static AppStat newSparkCount(int count) {
    AppStat appStat = new AppStat();
    appStat.setType(Type.SPARKS);
    appStat.setCount(count);
    return appStat;
  }

  public static AppStat newPostCount(int count) {
    AppStat appStat = new AppStat();
    appStat.setType(Type.POSTS);
    appStat.setCount(count);
    return appStat;
  }

}
