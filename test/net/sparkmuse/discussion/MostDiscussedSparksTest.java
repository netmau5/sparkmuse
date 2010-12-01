package net.sparkmuse.discussion;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import net.sparkmuse.data.entity.SparkVO;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Dec 1, 2010
 */
public class MostDiscussedSparksTest extends UnitTest {

  @Test
  public void comparatorShouldOrderByMostDiscussed() {
    final SparkVO spark1 = mock(SparkVO.class);
    when(spark1.getPostCount()).thenReturn(5);

    final SparkVO spark2 = mock(SparkVO.class);
    when(spark2.getPostCount()).thenReturn(10);

    final ArrayList<SparkVO> sparks = Lists.newArrayList(spark1, spark2);
    final MostDiscussedSparks.Comparator c = new MostDiscussedSparks.Comparator();
    Collections.sort(sparks, c);

    MatcherAssert.assertThat(sparks.get(0).getPostCount(), equalTo(10));
  }

}
