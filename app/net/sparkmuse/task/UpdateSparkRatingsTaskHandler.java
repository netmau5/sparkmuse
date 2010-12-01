package net.sparkmuse.task;

import com.google.common.base.Function;
import net.sparkmuse.data.SparkDao;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.SparkRanking;
import play.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 7, 2010
 */
public class UpdateSparkRatingsTaskHandler {

  private final SparkDao sparkDao;
  private final IssueTaskService taskService;

  public UpdateSparkRatingsTaskHandler(SparkDao sparkDao, IssueTaskService taskService) {
    this.sparkDao = sparkDao;
    this.taskService = taskService;
  }

  public void apply(String cursor) {
    Logger.info("Updating spark ratings.");
    final Function<SparkVO, SparkVO> transformation = newTransformer();

    final String newCursor = sparkDao.transform(transformation, cursor);
    if (StringUtils.isEmpty(newCursor)) {
      Logger.info("Completed spark ratings updateCache.");
    }
    else {
      Logger.warn("Spark ratings updateCache did not complete, issuing a new task to restart from " + newCursor);
      taskService.issueSparkRatingUpdateTask(newCursor);
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
