package filters;

import play.mvc.Controller;
import play.mvc.Before;
import play.Logger;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.entity.UserVO;
import controllers.Authorization;
import controllers.Landing;

/**
 * @author neteller
 * @created: Mar 10, 2011
 */
public class FoundryAuthorizationFilter extends Controller {

  /**
   * Ensure present user has USER access level
   */
  @Before
  public static void checkUserAuthorization() {
    if (!getAccessLevel().hasAuthorizationLevel(AccessLevel.FOUNDRY)) {
      Logger.info("Unauthorized access: User [" + Authorization.getUserFromSession() + "] to Resource [" + request.path + "]");
      Landing.index();
    }
  }

  public static AccessLevel getAccessLevel() {
    UserVO user = Authorization.getUserFromSession();
    return null == user ? AccessLevel.UNAUTHORIZED : user.getAccessLevel();
  }

}
