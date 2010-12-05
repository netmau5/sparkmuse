package controllers;

import play.mvc.Controller;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.discussion.SparkSearchRequest;

/**
 * Controller for the landing pages.  The landing page
 * is the page where users will see the introduction
 * to the site and be apply to apply for membership.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Landing extends SparkmuseController {

  public static void index() {
    UserVO userFromSession = Authorization.getUserFromSession();
    if (null != userFromSession && userFromSession.isAuthorizedFor(AccessLevel.USER)) Home.index();
		else render();
	}

}
