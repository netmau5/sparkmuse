package controllers;

import play.mvc.With;
import play.Play;
import filters.AuthorizationFilter;
import net.sparkmuse.activity.ActivityService;
import net.sparkmuse.activity.ActivityStream;
import net.sparkmuse.data.entity.UserProfile;

import javax.inject.Inject;
import java.util.List;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */

@With(AuthorizationFilter.class)
public class ActivityController extends SparkmuseController{

  @Inject static ActivityService activityService;

  public static void view() {
    ActivityStream stream = activityService.getActivity(Authorization.getUserFromSession());
    renderTemplate("Activity/activity.html", stream);
  }

}
