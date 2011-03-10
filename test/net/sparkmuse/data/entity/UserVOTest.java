package net.sparkmuse.data.entity;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class UserVOTest extends UnitTest {

  @Test
  public void shouldComputeNewUser() {
    final UserVO user = new UserVO();
    user.setFirstLogin(new DateTime(1295324525247L));
    MatcherAssert.assertThat(user.isNewUser(), equalTo(false));

    user.setFirstLogin(new DateTime());
    MatcherAssert.assertThat(user.isNewUser(), equalTo(true));
  }

  @Test
  public void shouldntShowExpiredNotifications() {
    UserVO user = new UserVO();
    Notification n1 = new Notification();
    n1.setExpireDate(new DateTime());
    user.addNotification(n1);
    Notification n2 = new Notification();
    n2.setExpireDate(new DateTime().plusDays(5));
    user.addNotification(n2);

    MatcherAssert.assertThat(user.getNotificationsToShow().size(), is(1));
  }

}
