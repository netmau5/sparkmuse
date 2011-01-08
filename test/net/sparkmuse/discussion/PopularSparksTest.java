package net.sparkmuse.discussion;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.common.Orderings;

import java.util.ArrayList;
import java.util.Collections;

import com.google.common.collect.Lists;

/**
 * @author neteller
 * @created: Dec 1, 2010
 */
public class PopularSparksTest extends UnitTest {

  @Test
  public void comparatorShouldOrderByMostPopular() {
    final SparkVO spark1 = mock(SparkVO.class);
    when(spark1.getRating()).thenReturn(5d);

    final SparkVO spark2 = mock(SparkVO.class);
    when(spark2.getRating()).thenReturn(10d);

    final ArrayList<SparkVO> sparks = Lists.newArrayList(spark1, spark2);
    final Orderings.ByRating c = new Orderings.ByRating();
    Collections.sort(sparks, c);

    MatcherAssert.assertThat(sparks.get(0).getRating(), equalTo(10d));
  }

}
