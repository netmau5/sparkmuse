package filters;

import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.entity.UserVO;

import play.mvc.Before;
import play.mvc.Controller;
import play.Logger;
import play.Play;
import controllers.Landing;
import controllers.Authorization;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 9, 2010
 */
public class AuthorizationFilter extends Controller {


  /**
   * Ensure present user has USER access level
   */
  @Before
  public static void checkUserAuthorization() {
    if (!getAccessLevel().hasAuthorizationLevel(AccessLevel.USER)) {
      Logger.info("Unauthorized access: User [" + Authorization.getUserFromSession() + "] to Resource [" + request.path + "]");
      Landing.index();
    }
  }

  public static AccessLevel getAccessLevel() {
    UserVO user = Authorization.getUserFromSession();
    return null == user ? AccessLevel.UNAUTHORIZED : user.getAccessLevel();
  }
}
