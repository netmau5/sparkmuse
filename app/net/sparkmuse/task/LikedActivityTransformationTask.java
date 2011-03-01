package net.sparkmuse.task;

import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.activity.ActivityService;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;

/**
 * @author neteller
 * @created: Feb 28, 2011
 */
public class LikedActivityTransformationTask extends TransformationTask<UserVote> {

  private final ObjectDatastore datastore;
  private final ActivityService activityService;

  @Inject
  public LikedActivityTransformationTask(ActivityService activityService, Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
    this.activityService = activityService;
  }

  protected UserVote transform(UserVote userVote) {
    return activityService.notify(userVote);
  }

  protected FindCommand.RootFindCommand<UserVote> find(boolean isNew) {
    FindCommand.RootFindCommand<UserVote> find = datastore.find().type(UserVote.class);

    final Migration lastMigration = lastMigration();
    if (null != lastMigration) {
      find.addFilter("created", Query.FilterOperator.GREATER_THAN, lastMigration.getStarted().getMillis());
    }

    return find;
  }

}
