package controllers;

import javax.inject.Inject;

import net.sparkmuse.task.*;
import play.mvc.Catch;
import play.Logger;
import play.Play;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Cursor;
import com.google.common.collect.Maps;
import com.google.common.base.Preconditions;
import com.google.apphosting.api.DeadlineExceededException;
import com.google.inject.Injector;
import org.apache.commons.lang.StringUtils;

/**
 * Controller for receiving asychronous task requests.
 *
 * @author neteller
 * @created: Jul 16, 2010
 */
public class Tasks extends SparkmuseController {

  @Inject static IssueTaskService taskService;
  @Inject static Injector injector;

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

  public static <T extends Task> void execute(String taskClassName, String cursor) {
    final Class<T> taskClass = getTaskClass(taskClassName);
    final T task = injector.getInstance(taskClass);
    final Cursor newCursor = task.execute(StringUtils.isNotBlank(cursor) ? Cursor.fromWebSafeString(cursor) : null);
    if (!task.isComplete()) { 
      Logger.info("Did not complete task [" + taskClass + "], issuing a new task to restart from cursor [" + newCursor.toWebSafeString() + "].");
      taskService.issue(taskClass, newCursor);
    }
    renderText("Success");
  }

  private static <T extends Task> Class<T> getTaskClass(String taskClassName) {
    try {
      final Class<T> taskClass = (Class<T>) Play.classloader.loadClass(taskClassName);
      Preconditions.checkState(Task.class.isAssignableFrom(taskClass));
      return taskClass;
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
