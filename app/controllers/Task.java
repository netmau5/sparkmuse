package controllers;

import javax.inject.Inject;

import net.sparkmuse.task.UpdateSparkRatingsTaskHandler;

/**
 * Controller for receiving asychronous task requests.
 *
 * @author neteller
 * @created: Jul 16, 2010
 */
public class Task extends SparkmuseController {

  @Inject static UpdateSparkRatingsTaskHandler updateSparkRatingsHandler;

  public static void updateSparkRatings(String cursor) {
    updateSparkRatingsHandler.apply(cursor);
  }

}
