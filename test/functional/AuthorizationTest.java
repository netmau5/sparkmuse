package functional;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import models.UserApplicationModel;
import play.mvc.Http.*;
import com.vercer.engine.persist.ObjectDatastore;

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

    UserApplicationModel model = datastore.find().type(UserApplicationModel.class)
        .addFilter("userName", EQUAL, "netmau5")
        .returnResultsNow()
        .next();
    assertEquals(model.userName, "netmau5");
    datastore.delete(model);
  }
}
