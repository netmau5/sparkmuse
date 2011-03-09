package net.sparkmuse.user;

import play.test.UnitTest;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import net.sparkmuse.data.entity.UserVO;

/**
 * @author neteller
 * @created: Mar 8, 2011
 */
public class SaltedPasswordTest extends UnitTest {

  @Test
  public void shouldVerifySaltedPassword() {
    SaltedPassword saltedDave = SaltedPassword.newPassword("dave");

    UserVO user = new UserVO();
    user.setSaltedPassword(saltedDave);

    MatcherAssert.assertThat(saltedDave.verifyPassword("dave"), is(true));
  }

}
