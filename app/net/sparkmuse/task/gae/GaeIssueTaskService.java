package net.sparkmuse.task.gae;

import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.data.Cacheable;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;
import com.google.common.collect.Maps;
import play.mvc.Router;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class GaeIssueTaskService implements IssueTaskService {

  private final Queue queue;

  public GaeIssueTaskService(final Queue queue) {
    this.queue = queue;
  }

  public <T extends Cacheable<T>> void issueCachePersistTask(Cacheable<T> cacheable) {
    final String key = cacheable.getKey().toString();

    final TaskOptions taskOptions = url(Router.reverse("Task.persistCacheValue").url);
    taskOptions.param("cacheKey", key);
    queue.add(taskOptions);
  }

  public void issueSparkRatingUpdateTask(String cursor) {
    final Map<String,Object> parameters = Maps.newHashMap();
    parameters.put("cursor", cursor);
    queue.add(url(Router.reverse(
        "Task.updateSparkRatings",
        parameters
    ).url));
  }
}
