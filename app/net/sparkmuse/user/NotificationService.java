package net.sparkmuse.user;

import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.SendNotificationTask;
import com.google.inject.Inject;
import com.google.common.collect.ImmutableMap;

/**
 * @author neteller
 * @created: Mar 8, 2011
 */
public class NotificationService {

  private final IssueTaskService issueTaskService;

  @Inject
  public NotificationService(IssueTaskService issueTaskService) {
    this.issueTaskService = issueTaskService;
  }

  public void issue(Long userId, String displayNotification) {
    this.issueTaskService.issue(
        SendNotificationTask.class,
        ImmutableMap.of(
            SendNotificationTask.NOTIFICATION_MESSAGE, displayNotification,
            SendNotificationTask.NOTIFICATION_USERID, userId
        ),
        null
    );
  }

}
