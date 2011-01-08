package net.sparkmuse.discussion;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import org.joda.time.Days;
import org.joda.time.DateTime;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.common.Orderings;

import java.util.ArrayList;
import java.util.Collections;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * @author neteller
 * @created: Dec 1, 2010
 */
public class RecentSparksTest extends UnitTest {

  @Test
  public void comparatorShouldOrderByMostRecent() {
    final SparkVO spark1 = mock(SparkVO.class);
    when(spark1.getCreated()).thenReturn(new DateTime().minusDays(5));

    final SparkVO spark2 = mock(SparkVO.class);
    when(spark2.getCreated()).thenReturn(new DateTime().minusDays(10));

    final ArrayList<SparkVO> sparks = Lists.newArrayList(spark1, spark2);
    final Ordering c = new Orderings.ByRecency<SparkVO>();
    Collections.sort(sparks, c);

    MatcherAssert.assertThat(sparks.get(0).getCreated(), greaterThan(sparks.get(1).getCreated()));
  }

}
