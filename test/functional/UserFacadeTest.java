package functional;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.data.entity.UserApplication;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class UserFacadeTest extends PluginFunctionalTest {

  private UserFacade userFacade;

  @Before
  public void setup() {
    super.setup();
    userFacade = FunctionalTestUtils.getInstance(UserFacade.class);
  }

  @After
  public void tearDown() {
    userFacade = null;
    super.tearDown();
  }

  @Test
  public void shouldSaveUserApplication() {
    userFacade.applyForInvitation("netmau5", "http://blog.sparkmuse.com");

    UserApplication model = datastore.find().type(UserApplication.class)
        .addFilter("userName", EQUAL, "netmau5")
        .now()
        .next();
    assertEquals(model.userName, "netmau5");
    datastore.delete(model);
  }


}
