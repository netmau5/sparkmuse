package controllers;

import javax.inject.Inject;

import net.sparkmuse.task.UpdateSparkRatingsTaskHandler;
import net.sparkmuse.task.PostCountRepairer;
import net.sparkmuse.task.IssueTaskService;
import play.mvc.Catch;
import play.Logger;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.common.collect.Maps;
import com.google.apphosting.api.DeadlineExceededException;

/**
 * Controller for receiving asychronous task requests.
 *
 * @author neteller
 * @created: Jul 16, 2010
 */
public class Task extends SparkmuseController {

  @Inject static UpdateSparkRatingsTaskHandler updateSparkRatingsHandler;
  @Inject static PostCountRepairer postCountRepairer;
  @Inject static IssueTaskService taskService;

  @Catch(DatastoreTimeoutException.class)
  static void handleTimeout(DatastoreTimeoutException e) {
    Logger.error(e, "Datastore timeout while processing task " + request.url + ", retrying now.");
    taskService.issue(request.action, Maps.<String, Object>newHashMap(request.params.allSimple()));
  }

  @Catch(DeadlineExceededException.class)
  static void handleDeadlineExceeded(DeadlineExceededException e) {
    Logger.error(e, "Deadline exceeded while processing task " + request.url + ", retrying now.");
    taskService.issue(request.action, Maps.<String, Object>newHashMap(request.params.allSimple()));
  }

  public static void updateSparkRatings(String cursor) {
    updateSparkRatingsHandler.execute(cursor);
  }

  public static void commentRepair(String cursor) {
    postCountRepairer.execute(cursor);
  }

}
