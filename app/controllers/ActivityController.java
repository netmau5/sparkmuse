package controllers;

import play.mvc.With;
import play.Play;
import play.data.validation.Required;
import filters.AuthorizationFilter;
import net.sparkmuse.activity.ActivityService;
import net.sparkmuse.activity.ActivityStream;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.discussion.SparkFacade;
import net.sparkmuse.ajax.FragmentAjaxResponse;

import javax.inject.Inject;
import java.util.List;

import com.google.common.collect.ImmutableMap;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */

@With(AuthorizationFilter.class)
public class ActivityController extends SparkmuseController{

  @Inject static ActivityService activityService;
  @Inject static SparkFacade sparkFacade;

  public static void view() {
    ActivityStream stream = activityService.getActivity(Authorization.getUserFromSession());
    renderTemplate("Activity/activity.html", stream);
  }

  public static void more(@Required Activity.Source source) {
    ActivityStream stream = activityService.getActivity(Authorization.getUserFromSession(), source);
    renderJSON(new FragmentAjaxResponse("tags/activities.html", ImmutableMap.<String, Object>of(
        "_arg", stream
    )));
  }

  public static void show(@Required Activity.Kind kind, @Required Long contentKey) {
    if (kind == Activity.Kind.SPARK) {
      SparkVO spark = sparkFacade.findSparkBy(contentKey);
      renderJSON(new FragmentAjaxResponse("Activity/spark.html", ImmutableMap.<String, Object>of(
          "spark", spark
      )));
    }
    else if (kind == Activity.Kind.POST) {
      Post post = sparkFacade.findPostBy(contentKey);
      renderJSON(new FragmentAjaxResponse("Activity/post.html", ImmutableMap.<String, Object>of(
          "post", post
      )));
    }
  }

}
