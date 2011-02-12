package net.sparkmuse.task;

import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.twig.DatastoreUtils;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.mail.*;
import net.sparkmuse.activity.ActivityService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import com.google.inject.Inject;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Query;
import play.Logger;

import java.util.List;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class ActivityUpdateTransformationTask extends TransformationTask<Post> {

  private final ObjectDatastore datastore;
  private final ActivityService activityService;

  @Inject
  public ActivityUpdateTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, ActivityService activityService) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
    this.activityService = activityService;
  }

  protected String getTaskName() {
    return "Post Activity Update Task";
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
