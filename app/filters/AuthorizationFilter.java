package filters;

import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.Constants;

import play.mvc.Before;
import play.mvc.Controller;
import play.Logger;
import play.Play;
import controllers.Landing;
import controllers.Authorization;
import controllers.Application;

import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 9, 2010
 */
public class AuthorizationFilter extends Controller {

  @Inject
  static Cache cache;

  /**
   * Ensure present user has USER access level
   */
  @Before
  public static void checkUserAuthorization() {
    if (!getAccessLevel().hasAuthorizationLevel(AccessLevel.USER)) {
      Logger.info("Unauthorized access: User [" + Authorization.getUserFromSession() + "] to Resource [" + request.path + "]");
      cache.set(
          Constants.AFTER_LOGIN_REDIRECT_PATH_CACHE_PREFIX + session.getId(),
          request.url
      );
      Authorization.unauthorized();
    }
  }

  public static AccessLevel getAccessLevel() {
    UserVO user = Authorization.getUserFromSession();
    return null == user ? AccessLevel.UNAUTHORIZED : user.getAccessLevel();
  }

}
