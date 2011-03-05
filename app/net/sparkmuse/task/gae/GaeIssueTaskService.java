package net.sparkmuse.task.gae;

import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.Task;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.NullTo;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import play.mvc.Router;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class GaeIssueTaskService implements IssueTaskService {

  private final Queue queue;

  @Inject
  public GaeIssueTaskService(final Queue queue) {
    this.queue = queue;
  }

  public <T extends Cacheable<T>> void issueCachePersistTask(Cacheable<T> cacheable) {
    final String key = cacheable.getKey().toString();

    final TaskOptions taskOptions = url(Router.reverse("Tasks.persistCacheValue").url);
    taskOptions.param("cacheKey", key);
    queue.add(taskOptions);
  }

  public void issue(String action, Map<String, Object> parameters) {
    TaskOptions options = url(Router.reverse(action).url);

    for (Map.Entry<String, Object> entry: parameters.entrySet()) {
      if (null != entry.getValue()) options.param(entry.getKey(), entry.getValue().toString());
    }

    queue.add(options);
  }

  public <T extends Task> void issue(Class<T> taskClass, @Nullable Cursor cursor) {
    TaskOptions options = url(Router.reverse("Tasks.execute").url);

    options.param("taskClassName", taskClass.getName());
    if (null != cursor) options.param("cursor", cursor.toWebSafeString());

    queue.add(options);
  }

  public <T extends Task> void issue(Class<T> taskClass, Map<?, ?> parameters, @Nullable Cursor cursor) {
    TaskOptions options = url(Router.reverse("Tasks.execute").url);

    for (Map.Entry entry: parameters.entrySet()) {
      options.param(entry.getKey().toString(), entry.getValue().toString());
    }

    options.param("taskClassName", taskClass.getName());
    if (null != cursor) options.param("cursor", cursor.toWebSafeString());


    queue.add(options);
  }
}
