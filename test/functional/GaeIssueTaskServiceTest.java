package functional;

import org.junit.Test;
import org.mockito.Mockito;
import play.mvc.Router;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.Queue;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.task.gae.GaeIssueTaskService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class GaeIssueTaskServiceTest extends PluginFunctionalTest {

  @Test
  public void shouldAddPersistenceTask() {
    final UserVO user = new UserVO();
    user.setId(123L);

    final Queue queue = Mockito.mock(Queue.class);
    final TaskOptions taskOptions = url(Router.reverse(
        "Tasks.persistCacheValue"
    ).url);
    taskOptions.param("cacheKey", user.getKey().toString());
    final GaeIssueTaskService taskService = new GaeIssueTaskService(queue);
    taskService.issueCachePersistTask(user);

    Mockito.verify(queue).add(Mockito.refEq(taskOptions));
  }

}
