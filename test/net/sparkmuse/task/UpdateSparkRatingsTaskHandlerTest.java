package net.sparkmuse.task;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import net.sparkmuse.data.SparkDao;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.SparkRanking;
import com.google.common.base.Function;
import play.test.UnitTest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 8, 2010
 */
public class UpdateSparkRatingsTaskHandlerTest extends UnitTest {

  @Test
  public void shouldBeginTransformFromNullCursor() {
    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
    final UpdateSparkRatingsTaskHandler handler = new UpdateSparkRatingsTaskHandler(sparkDao, Mockito.mock(IssueTaskService.class));
    handler.apply(null);
    Mockito.verify(sparkDao).transform(Mockito.any(Function.class), (String) Mockito.eq(null));
  }

  @Test
  public void shouldContinueFromGivenCursor() {
    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
    final UpdateSparkRatingsTaskHandler handler = new UpdateSparkRatingsTaskHandler(sparkDao, Mockito.mock(IssueTaskService.class));
    handler.apply("cursor");
    Mockito.verify(sparkDao).transform(Mockito.any(Function.class), (String) Mockito.eq("cursor"));
  }

  @Test
  public void shouldIssueNewSparkUpdateTaskWhenCursorReturned() {
    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
    final IssueTaskService issueTaskService = Mockito.mock(IssueTaskService.class);
    Mockito.when(sparkDao.transform(Mockito.any(Function.class), Mockito.any(String.class)))
        .thenReturn("cursor");

    final UpdateSparkRatingsTaskHandler handler = new UpdateSparkRatingsTaskHandler(sparkDao, issueTaskService);
    handler.apply(null);
    Mockito.verify(issueTaskService).issueSparkRatingUpdateTask("cursor");
  }

  @Test
  public void transformationShouldSetSparkRating() {
    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
    final UpdateSparkRatingsTaskHandler handler = new UpdateSparkRatingsTaskHandler(sparkDao, Mockito.mock(IssueTaskService.class));

    final Function<SparkVO, SparkVO> transformation = handler.newTransformer();
    final SparkVO sparkVO = new SparkVO();
    sparkVO.setCreated(new DateTime().minusDays(1));
    sparkVO.setVotes(20);
    transformation.apply(sparkVO);

    MatcherAssert.assertThat(SparkRanking.calculateRating(sparkVO), Matchers.equalTo(sparkVO.getRating()));
  }
  
}
