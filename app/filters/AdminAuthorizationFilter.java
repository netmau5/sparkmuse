package filters;

import play.mvc.Before;
import play.mvc.Controller;
import play.Logger;
import net.sparkmuse.data.util.AccessLevel;
import controllers.Authorization;
import controllers.Landing;

/**
 * @author neteller
 * @created: Dec 20, 2010
 */
public class AdminAuthorizationFilter extends Controller {

  /**
   * Ensure present user has USER access level
   */
  @Before
  public static void checkUserAuthorization() {
    if (!AuthorizationFilter.getAccessLevel().hasAuthorizationLevel(AccessLevel.ADMIN)) {
      Logger.info("Unauthorized access: User [" + Authorization.getUserFromSession() + "] to Resource [" + request.path + "]");
      Landing.index();
    }
  }

}
