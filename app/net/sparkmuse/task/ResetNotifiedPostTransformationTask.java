package net.sparkmuse.task;

import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.activity.ActivityService;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Cursor;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import com.google.common.base.Function;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public class ResetNotifiedPostTransformationTask extends Task {

  private final ObjectDatastore datastore;
  private final BatchDatastoreService batchService;
  private final Cache cache;

  @Inject
  public ResetNotifiedPostTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(datastore);
    this.datastore = datastore;
    this.batchService = batchService;
    this.cache = cache;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    datastore.deleteAll(Activity.class);
    datastore.deleteAll(datastore.find().type(Migration.class)
      .addFilter("taskName", Query.FilterOperator.EQUAL, PostActivityTransformationTask.TASK_NAME)
      .returnAll()
      .now());
    datastore.deleteAll(datastore.find().type(Migration.class)
      .addFilter("taskName", Query.FilterOperator.EQUAL, SparkActivityTransformationTask.TASK_NAME)
      .returnAll()
      .now());

    batchService.transform(
        datastore.find().type(Post.class).addFilter("notified", Query.FilterOperator.EQUAL, Boolean.TRUE),
        new Function<Post, Post>() {
          public Post apply(Post post) {
            post.setNotified(false);
            return post;
          }
        },
        null
    );
    batchService.transform(
        datastore.find().type(SparkVO.class).addFilter("notified", Query.FilterOperator.EQUAL, Boolean.TRUE),
        new Function<SparkVO, SparkVO>() {
          public SparkVO apply(SparkVO spark) {
            spark.setNotified(false);
            return spark;
          }
        },
        null
    );

    cache.delete(ActivityService.GLOBAL_ACTIVITY);
    return null;
  }

}
