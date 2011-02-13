package controllers;

import play.mvc.With;
import filters.AuthorizationFilter;
import net.sparkmuse.activity.ActivityService;

import javax.inject.Inject;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */

@With(AuthorizationFilter.class)
public class ActivityController extends SparkmuseController{

  @Inject static ActivityService activityService;

  public static void view() {
    
  }

}
