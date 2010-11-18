package functional;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import net.sparkmuse.user.UserFacade;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import models.UserApplicationModel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class UserServiceTest extends PluginFunctionalTest {

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

    UserApplicationModel model = datastore.find().type(UserApplicationModel.class)
        .addFilter("userName", EQUAL, "netmau5")
        .returnResultsNow()
        .next();
    assertEquals(model.userName, "netmau5");
    datastore.delete(model);
  }


}
