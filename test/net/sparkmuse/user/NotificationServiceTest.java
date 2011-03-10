package net.sparkmuse.user;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.SendNotificationTask;
import com.google.common.collect.ImmutableMap;

/**
 * @author neteller
 * @created: Mar 8, 2011
 */
public class NotificationServiceTest extends UnitTest {

  @Test
  public void shouldIssueNotification() {
    IssueTaskService issueTaskService = mock(IssueTaskService.class);
    NotificationService notificationService = new NotificationService(issueTaskService);

    Long userId = 1L;
    String displayNotification = "Hello World!";
    notificationService.issue(userId, displayNotification);

    verify(issueTaskService).issue(
        SendNotificationTask.class,
        ImmutableMap.of(
            SendNotificationTask.NOTIFICATION_MESSAGE, displayNotification,
            SendNotificationTask.NOTIFICATION_USERID, userId
        ),
        null
    );
  }

}
