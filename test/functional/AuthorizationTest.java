package functional;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import org.junit.Test;
import play.mvc.Http.*;
import net.sparkmuse.data.entity.UserApplication;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 9, 2010
 */
public class AuthorizationTest extends PluginFunctionalTest {

  @Test
  public void testApplyForInvitation() {
    Response response = GET("/Authorization/applyForInvitation?userName=netmau5&url=http://blog.sparkmuse.com");
    assertIsOk(response);

    UserApplication model = datastore.find().type(UserApplication.class)
        .addFilter("userName", EQUAL, "netmau5")
        .now()
        .next();
    assertEquals(model.userName, "netmau5");
    datastore.delete(model);
  }
}
