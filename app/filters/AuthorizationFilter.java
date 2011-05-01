package filters;

import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.Constants;
import net.sparkmuse.common.AccessibleBy;

import play.mvc.Before;
import play.mvc.Controller;
import play.Logger;
import play.Play;
import controllers.Landing;
import controllers.Authorization;
import controllers.Application;

import javax.inject.Inject;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

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
    AccessLevel requiredAccessLevel = determineAccessLevel();
    if (!getAccessLevel().hasAuthorizationLevel(requiredAccessLevel)) {
      Logger.info("Unauthorized access: User [" + Authorization.getUserFromSession() + "] to Resource [" + request.path + "]");
      if (!StringUtils.equals(flash.get(Constants.REDIRECTING), "TRUE")) {
        cache.set(
            Constants.AFTER_LOGIN_REDIRECT_PATH_CACHE_PREFIX + session.getId(),
            request.url
        );
      }
      Authorization.unauthorized();
    }
  }

  public static AccessLevel getAccessLevel() {
    UserVO user = Authorization.getUserFromSession();
    return null == user ? AccessLevel.UNAUTHORIZED : user.getAccessLevel();
  }

  private static AccessLevel determineAccessLevel() {
    AccessibleBy classAccessibleBy = request.controllerClass.getAnnotation(AccessibleBy.class);
    try {
      Method method = request.invokedMethod;
      AccessibleBy methodAccessibleBy = method.getAnnotation(AccessibleBy.class);
      if (null != methodAccessibleBy) return methodAccessibleBy.value();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
    if (null != classAccessibleBy) return classAccessibleBy.value();
    return AccessLevel.USER;
  }

}
