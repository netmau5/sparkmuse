package controllers;

import play.mvc.With;
import play.Play;
import filters.AuthorizationFilter;
import net.sparkmuse.data.entity.UserProfile;

import java.util.List;

/**
 * @author neteller
 * @created: Feb 13, 2011
 */
@With(AuthorizationFilter.class)
public class People extends SparkmuseController {

  public static void view() {
    List<UserProfile> profiles = userFacade.getAllProfiles();
    renderTemplate("People/people.html", profiles);
  }

}
