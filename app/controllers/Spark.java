package controllers;

import play.mvc.Router;
import play.mvc.With;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.data.validation.Valid;

import java.util.HashMap;

import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import net.sparkmuse.discussion.SparkFacade;
import net.sparkmuse.discussion.SparkSearchRequest;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.user.Votables;
import com.google.common.collect.Maps;

import javax.inject.Inject;

import filters.AuthorizationFilter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
@With(AuthorizationFilter.class)
public class Spark extends SparkmuseController {

  @Inject static SparkFacade sparkFacade;

  public static void create() {
    render();
  }

  public static void submit(@Valid @Required(message="Input is required.") SparkVO newSpark) {
    if (Validation.hasErrors()) {
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }
    else {
      newSpark.setAuthor(Authorization.getUserFromSessionOrAuthenticate(true));
      final SparkVO savedSpark = sparkFacade.createSpark(newSpark);
      final HashMap<String, Object> parameters = Maps.newHashMap();
      parameters.put("sparkId", savedSpark.getId());
      renderJSON(new RedirectAjaxResponse(Router.reverse("Spark.view", parameters).url));
    }
  }

  public static void view(final Long sparkId) {
    if (null == sparkId) Home.index(SparkSearchRequest.Filter.RECENT);
    final SparkVO spark = sparkFacade.findSparkBy(sparkId);
    final UserVotes userVotes = userFacade.findUserVotesFor(Votables.collect(spark), Authorization.getUserFromSession());
    render(spark, userVotes);
  }
  
}
