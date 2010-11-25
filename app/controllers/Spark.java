package controllers;

import play.mvc.Controller;
import play.mvc.Router;
import play.data.validation.Required;
import play.data.validation.MinSize;
import play.data.validation.Validation;
import play.data.validation.Valid;

import java.util.List;
import java.util.HashMap;

import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.ajax.RedirectAjaxResponse;
import net.sparkmuse.discussion.SparkFacade;
import net.sparkmuse.data.entity.SparkVO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.base.Splitter;

import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Spark extends SparkmuseController {

  @Inject static SparkFacade sparkFacade;

  public static void create() {
    render();
  }

//  public static void submit(
//      @Required final String title,
//      @Required final String stage,
//      @Required final String problem,
//      @Required final String solution,
//      @Required final String tags
//  ) {
  public static void submit(@Valid @Required(message="Input is required.") SparkVO newSpark) {
    if (Validation.hasErrors()) {
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }
    else {
      newSpark.setAuthor(Authorization.getUserFromSessionOrAuthenticate(true));
      final SparkVO savedSpark = sparkFacade.createSpark(newSpark);
      final HashMap<String,Object> parameters = Maps.newHashMap();
      parameters.put("sparkId", savedSpark.getId());
      renderJSON(new RedirectAjaxResponse(Router.reverse("Spark.view", parameters).url));
    }
  }

  public static void view(final long sparkId) {
    //@todo
//    final SparkVO spark = sparkFacade.findSparkBy(sparkId);
//    render(spark);
    render();
  }
  
}
