package net.sparkmuse.task;

import org.junit.Test;
import org.mockito.Mockito;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.common.Cache;
import com.google.common.base.Function;
import com.google.appengine.api.datastore.Cursor;
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
    final BatchDatastoreService batch = Mockito.mock(BatchDatastoreService.class);
    final UpdateSparkRatingsTask handler = new UpdateSparkRatingsTask(
        Mockito.mock(IssueTaskService.class),
        batch,
        Mockito.mock(Cache.class)
    );
    handler.execute(null);
    Mockito.verify(batch).transform(Mockito.any(Function.class), SparkVO.class, (Cursor) Mockito.eq(null));
  }

  @Test
  public void shouldContinueFromGivenCursor() {
    final BatchDatastoreService batch = Mockito.mock(BatchDatastoreService.class);
    final UpdateSparkRatingsTask handler = new UpdateSparkRatingsTask(
        Mockito.mock(IssueTaskService.class),
        batch,
        Mockito.mock(Cache.class)
    );
    handler.execute("cursor");
    Mockito.verify(batch).transform(Mockito.any(Function.class), SparkVO.class, Mockito.eq(Cursor.fromWebSafeString("cursor")));
  }

//  @Test
//  public void shouldIssueNewSparkUpdateTaskWhenCursorReturned() {
//    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
//    final IssueTaskService issueTaskService = Mockito.mock(IssueTaskService.class);
//    Mockito.when(sparkDao.transform(Mockito.any(Function.class), Mockito.any(String.class)))
//        .thenReturn("cursor");
//
//    final UpdateSparkRatingsTask handler = new UpdateSparkRatingsTask(sparkDao, issueTaskService);
//    handler.execute(null);
//    Mockito.verify(issueTaskService).issueSparkRatingUpdate("cursor");
//  }
//
//  @Test
//  public void transformationShouldSetSparkRating() {
//    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
//    final UpdateSparkRatingsTask handler = new UpdateSparkRatingsTask(sparkDao, Mockito.mock(IssueTaskService.class));
//
//    final Function<SparkVO, SparkVO> transformation = handler.newTransformer();
//    final SparkVO sparkVO = new SparkVO();
//    sparkVO.setCreated(new DateTime().minusDays(1));
//    sparkVO.setVotes(20);
//    transformation.apply(sparkVO);
//
//    MatcherAssert.assertThat(SparkRanking.calculateRating(sparkVO), Matchers.equalTo(sparkVO.getRating()));
//  }
  
}
