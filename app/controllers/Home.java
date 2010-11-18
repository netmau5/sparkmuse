package controllers;

import play.mvc.Controller;
import play.mvc.With;
import filters.AuthorizationFilter;

/**
 * Controller for the home page.  The home page is where
 * the user will see a list of popular and new Sparks.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
@With(AuthorizationFilter.class)
public class Home extends SparkmuseController {

  public static void index() {
    render();
  }

}
