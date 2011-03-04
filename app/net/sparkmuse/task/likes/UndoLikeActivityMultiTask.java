package net.sparkmuse.task.likes;

import net.sparkmuse.task.*;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.activity.ActivityService;
import com.google.inject.Inject;
import com.google.code.twig.ObjectDatastore;
import com.google.common.collect.Lists;
import com.google.appengine.api.datastore.Query;

import java.util.List;

/**
 * @author neteller
 * @created: Mar 4, 2011
 */
public class UndoLikeActivityMultiTask extends MultiTask {

  private final ObjectDatastore datastore;
  private final ActivityService activityService;
  private final Cache cache;
  private final BatchDatastoreService batchService;

  @Inject
  public UndoLikeActivityMultiTask(ObjectDatastore datastore,
                                   ActivityService activityService,
                                   Cache cache,
                                   BatchDatastoreService batchService,
                                   IssueTaskService issueTaskService) {
    super(datastore, issueTaskService);
    this.datastore = datastore;
    this.activityService = activityService;
    this.cache = cache;
    this.batchService = batchService;
  }

  protected Iterable<Class> getTasks() {
    return Lists.<Class>newArrayList(
        ResetNotifiedUserVoteTransformationTask.class,
        RemoveLikedActivityTransformationTask.class
    );
  }

  @Override
  protected void onBegin() {
    LikedActivityTransformationTask likedTransformation = new LikedActivityTransformationTask(activityService, cache, batchService, datastore);
    List<Migration> migrations = datastore.find().type(Migration.class)
        .addFilter("taskName", Query.FilterOperator.EQUAL, likedTransformation.getTaskName())
        .returnAll()
        .now();
    datastore.deleteAll(migrations);
  }
}
