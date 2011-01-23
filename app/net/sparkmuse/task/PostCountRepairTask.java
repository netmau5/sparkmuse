package net.sparkmuse.task;

import net.sparkmuse.common.Cache;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import net.sparkmuse.data.twig.BatchDatastoreService;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Cursor;
import com.google.common.base.Function;
import play.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Reports and repairs the comment counts on Sparks.
 *
 * @author neteller
 * @created: Jan 17, 2011
 */
public class PostCountRepairTask {

  private final Cache cache;
  private final ObjectDatastore datastore;
  private final IssueTaskService taskService;
  private final BatchDatastoreService batch;

  @Inject
  public PostCountRepairTask(Cache cache, ObjectDatastore datastore, IssueTaskService taskService, BatchDatastoreService batch) {
    this.cache = cache;
    this.datastore = datastore;
    this.taskService = taskService;
    this.batch = batch;
  }

  public void execute(String cursorString) {
    Logger.info("Updating spark post count.");

    final Cursor cursor = StringUtils.isNotBlank(cursorString) ? Cursor.fromWebSafeString(cursorString) : null;
    final Cursor newCursor = batch.transform(createTransformation(), SparkVO.class, cursor);
    if (null == cursor) {
      cleanup();
      Logger.info("Completed updating spark post count.");
    }
    else {
      Logger.info("Did not complete updating spark post count, issuing a new task to restart from " + newCursor);
      taskService.issueSparkRatingUpdate(newCursor.toWebSafeString());
    }
  }

  private Function<SparkVO, SparkVO> createTransformation() {
    final Function<SparkVO, SparkVO> transformation = new Function<SparkVO, SparkVO>() {
      public SparkVO apply(SparkVO sparkVO) {
        final Integer count = datastore.find()
            .type(Post.class)
            .addFilter("sparkId", EQUAL, sparkVO.getId())
            .returnCount()
            .now();

        if (sparkVO.getPostCount() != count) {
          Logger.error("Spark [" + sparkVO.getTitle() + "] reported a post count of [" + sparkVO.getPostCount() + "] but had [" + count + "] posts.");
          sparkVO.setPostCount(count);
        }

        return sparkVO;
      }
    };
    return transformation;
  }

  private void cleanup() {
    cache.clear();
  }

}
