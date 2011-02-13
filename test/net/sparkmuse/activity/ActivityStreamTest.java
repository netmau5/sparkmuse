package net.sparkmuse.activity;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;
import net.sparkmuse.data.entity.Activity;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public class ActivityStreamTest extends UnitTest {

  @Test
  public void shouldOverlayStreams() {
    Activity firstActivity = new Activity();
    firstActivity.setCreated(new DateTime().minusDays(2));
    firstActivity.setKind(Activity.Kind.POST);
    firstActivity.setContentKey(1L);
    firstActivity.setId(1L);

    Activity secondActivity = new Activity();
    secondActivity.setCreated(new DateTime().minusDays(1));
    secondActivity.setKind(Activity.Kind.POST);
    secondActivity.setContentKey(2L);
    secondActivity.setId(2L);

    ActivityStream s1 = new ActivityStream(Lists.newArrayList(secondActivity, firstActivity));

    Activity thirdActivity = new Activity();
    thirdActivity.setCreated(new DateTime().minusDays(3));
    thirdActivity.setKind(Activity.Kind.POST);
    thirdActivity.setContentKey(4L);
    thirdActivity.setId(4L);

    //duplicate of secondActivity
    Activity fourthActivity = new Activity();
    fourthActivity.setCreated(new DateTime().minusDays(1));
    fourthActivity.setKind(Activity.Kind.POST);
    fourthActivity.setContentKey(2L);
    fourthActivity.setId(4L);
    fourthActivity.setPopulation(Activity.Population.USER);

    ActivityStream s2 = new ActivityStream(Lists.newArrayList(fourthActivity, thirdActivity));

    //s1 is indicative of everyone, s2 of user
    ActivityStream newStream = s1.overlay(s2);

    //fourth activity should be first and should have overriden second activity
    MatcherAssert.assertThat(Iterables.get(newStream.getActivities(), 0).getPopulation(), equalTo(Activity.Population.USER));
    MatcherAssert.assertThat(newStream.getActivities().size(), is(3));
  }

}
