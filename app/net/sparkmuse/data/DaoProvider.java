package net.sparkmuse.data;

import com.google.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class DaoProvider {

  private final PostDao postDao;
  private final UserDao userDao;
  private final SparkDao sparkDao;
  private final ActivityDao activityDao;

  @Inject
  public DaoProvider(PostDao postDao, UserDao userDao, SparkDao sparkDao, ActivityDao activityDao) {
    this.postDao = postDao;
    this.userDao = userDao;
    this.sparkDao = sparkDao;
    this.activityDao = activityDao;
  }

  public PostDao getPostDao() {
    return postDao;
  }

  public UserDao getUserDao() {
    return userDao;
  }

  public SparkDao getSparkDao() {
    return sparkDao;
  }

  public ActivityDao getActivityDao() {
    return activityDao;
  }

  public CrudDao getCrudDao() {
    return userDao;
  }

}
