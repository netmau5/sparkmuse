package net.sparkmuse.data.entity;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;
import com.google.common.collect.Sets;
import com.google.common.collect.Iterables;

import java.util.HashSet;
import java.util.Set;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public class ActivityTest extends UnitTest {

  @Test
  public void shouldCompareForOverlay() {
    Activity firstActivity = new Activity();
    firstActivity.setCreated(new DateTime().minusDays(2));
    firstActivity.setKind(Activity.Kind.POST);
    firstActivity.setContentKey(1L);
    firstActivity.setId(1L);

    Activity secondActivity = new Activity();
    secondActivity.setCreated(new DateTime().minusDays(1));
    secondActivity.setKind(Activity.Kind.POST);
    secondActivity.setContentKey(1L);
    secondActivity.setId(2L);

    Activity thirdActivity = new Activity();
    thirdActivity.setCreated(new DateTime());
    thirdActivity.setKind(Activity.Kind.POST);
    thirdActivity.setContentKey(2L);
    thirdActivity.setId(3L);

    //same kind/key should be equal
    MatcherAssert.assertThat(firstActivity.compareTo(secondActivity), is(0));

    //different key should be by time, first time is less than third time (measured as greater bc of desc order set)
    MatcherAssert.assertThat(firstActivity.compareTo(thirdActivity), greaterThan(0));

    Set<Activity> set = Sets.newTreeSet();
    set.add(firstActivity);
    set.add(secondActivity);
    set.add(thirdActivity);

    //first and second are the same, but first is added first so it should be in the set
    //first should be last because it has the oldest time, order should be most recent first
    MatcherAssert.assertThat(Iterables.getLast(set), equalTo(firstActivity));

    //2 are equal according to compareTo
    MatcherAssert.assertThat(Iterables.size(set), equalTo(2));
  }

}
