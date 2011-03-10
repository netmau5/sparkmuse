package functional.tasks;

import org.junit.Test;
import org.mockito.Mockito;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.common.Cache;
import net.sparkmuse.task.UpdateSparkRatingsTransformationTask;
import com.google.common.base.Function;
import com.google.appengine.api.datastore.Cursor;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import play.test.UnitTest;
import play.test.FunctionalTest;
import functional.FunctionalTestUtils;
import functional.PluginFunctionalTest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 8, 2010
 */
public class UpdateSparkRatingsTaskHandlerTest extends PluginFunctionalTest {

  @Test
  public void shouldBeginTransformFromNullCursor() {
    final BatchDatastoreService batch = Mockito.mock(BatchDatastoreService.class);
    final UpdateSparkRatingsTransformationTask handler = new UpdateSparkRatingsTransformationTask(
        Mockito.mock(Cache.class),
        batch,
        FunctionalTestUtils.getInstance(ObjectDatastore.class)
    );
    handler.execute(null);
    Mockito.verify(batch).transform(Mockito.any(FindCommand.RootFindCommand.class), Mockito.any(Function.class), (Cursor) Mockito.eq(null));
  }

  @Test
  public void shouldContinueFromGivenCursor() {
    final BatchDatastoreService batch = Mockito.mock(BatchDatastoreService.class);
    final UpdateSparkRatingsTransformationTask handler = new UpdateSparkRatingsTransformationTask(
        Mockito.mock(Cache.class),
        batch,
        FunctionalTestUtils.getInstance(ObjectDatastore.class)
    );
    Cursor cursor = Cursor.fromWebSafeString("cursor");
    handler.execute(cursor);
    Mockito.verify(batch).transform(Mockito.any(FindCommand.RootFindCommand.class), Mockito.any(Function.class), Mockito.eq(cursor));
  }

//  @Test
//  public void shouldIssueNewSparkUpdateTaskWhenCursorReturned() {
//    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
//    final IssueTaskService issueTaskService = Mockito.mock(IssueTaskService.class);
//    Mockito.when(sparkDao.transform(Mockito.any(Function.class), Mockito.any(String.class)))
//        .thenReturn("cursor");
//
//    final UpdateSparkRatingsTransformationTask handler = new UpdateSparkRatingsTransformationTask(sparkDao, issueTaskService);
//    handler.execute(null);
//    Mockito.verify(issueTaskService).issueSparkRatingUpdate("cursor");
//  }
//
//  @Test
//  public void transformationShouldSetSparkRating() {
//    final SparkDao sparkDao = Mockito.mock(SparkDao.class);
//    final UpdateSparkRatingsTransformationTask handler = new UpdateSparkRatingsTransformationTask(sparkDao, Mockito.mock(IssueTaskService.class));
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
