package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.common.collect.Lists;
import com.google.appengine.api.datastore.Query;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.activity.ActivityService;
import net.sparkmuse.common.Cache;

import java.util.List;

/**
 * @author neteller
 * @created: Feb 13, 2011
 */
public class ActivityUpdateMultiTask extends MultiTask {

  @Inject
  public ActivityUpdateMultiTask(ObjectDatastore datastore, IssueTaskService issueTaskService) {
    super(datastore, issueTaskService);
  }

  protected Iterable<Class> getTasks() {
    List<Class> tasks = Lists.<Class>newArrayList(
        SparkTransformationTask.class,
        PostTransformationTask.class
    );
    return tasks;
  }
  
  public static class PostTransformationTask extends TransformationTask<Post> {

    public static final String TASK_NAME = "Post Activity Update Task";

    private final ObjectDatastore datastore;
    private final ActivityService activityService;

    @Inject
    public PostTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, ActivityService activityService) {
      super(cache, batchService, datastore);
      this.datastore = datastore;
      this.activityService = activityService;
    }

    protected String getTaskName() {
      return TASK_NAME;
    }

    @Override
    protected FindCommand.RootFindCommand<Post> find(boolean isNew) {
      FindCommand.RootFindCommand<Post> find = datastore.find().type(Post.class);

      final Migration lastMigration = lastMigration();
      if (null != lastMigration) {
        find.addFilter("created", Query.FilterOperator.GREATER_THAN, lastMigration.getStarted().getMillis());
      }

      return find;
    }

    protected Post transform(Post post) {
      activityService.notify(post);

      return post;
    }

  }

  public static class SparkTransformationTask extends TransformationTask<SparkVO> {

    public static final String TASK_NAME = "Spark Activity Update Task";

    private final ObjectDatastore datastore;
    private final ActivityService activityService;

    @Inject
    public SparkTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, ActivityService activityService) {
      super(cache, batchService, datastore);
      this.datastore = datastore;
      this.activityService = activityService;
    }

    protected String getTaskName() {
      return TASK_NAME;
    }

    @Override
    protected FindCommand.RootFindCommand<SparkVO> find(boolean isNew) {
      FindCommand.RootFindCommand<SparkVO> find = datastore.find().type(SparkVO.class);

      final Migration lastMigration = lastMigration();
      if (null != lastMigration) {
        find.addFilter("created", Query.FilterOperator.GREATER_THAN, lastMigration.getStarted().getMillis());
      }

      return find;
    }

    protected SparkVO transform(SparkVO sparkVO) {
      activityService.notify(sparkVO);
      return sparkVO;
    }

  }

}
