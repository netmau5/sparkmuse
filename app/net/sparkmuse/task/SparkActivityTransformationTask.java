package net.sparkmuse.task;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.activity.ActivityService;
import net.sparkmuse.common.Cache;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;

/**
 * @author neteller
 * @created: Feb 13, 2011
 */
public class SparkActivityTransformationTask extends TransformationTask<SparkVO> {

  public static final String TASK_NAME = "Spark Activity Update Task";

  private final ObjectDatastore datastore;
  private final ActivityService activityService;

  @Inject
  public SparkActivityTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, ActivityService activityService) {
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