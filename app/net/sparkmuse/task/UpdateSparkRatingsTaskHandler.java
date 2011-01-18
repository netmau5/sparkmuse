package net.sparkmuse.task;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Cursor;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.SparkRanking;
import net.sparkmuse.common.Cache;
import play.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 7, 2010
 */
public class UpdateSparkRatingsTaskHandler {

  private final IssueTaskService taskService;
  private final BatchDatastoreService batch;
  private final Cache cache;

  @Inject
  public UpdateSparkRatingsTaskHandler(IssueTaskService taskService, BatchDatastoreService batch, Cache cache) {
    this.taskService = taskService;
    this.batch = batch;
    this.cache = cache;
  }

  public void execute(String cursorString) {
    Logger.info("Updating spark ratings.");
    final Function<SparkVO, SparkVO> transformation = newTransformer();

    final Cursor cursor = StringUtils.isNotBlank(cursorString) ? Cursor.fromWebSafeString(cursorString) : null;
    final Cursor newCursor = batch.transform(transformation, SparkVO.class, cursor);
    if (null == newCursor) {
      cache.clear();
      Logger.info("Completed updating spark ratings.");
    }
    else {
      Logger.info("Did not complete updating spark ratings, issuing a new task to restart from " + newCursor);
      taskService.issueSparkRatingUpdate(newCursor.toWebSafeString());
    }
  }

  Function<SparkVO, SparkVO> newTransformer() {
    final Function<SparkVO, SparkVO> transformation = new Function<SparkVO, SparkVO>() {
      public SparkVO apply(SparkVO sparkVO) {
        sparkVO.setRating(SparkRanking.calculateRating(sparkVO));
        return sparkVO;
      }
    };
    return transformation;
  }
}
