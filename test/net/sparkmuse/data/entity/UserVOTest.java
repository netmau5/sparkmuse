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

}
