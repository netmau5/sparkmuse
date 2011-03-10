package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;
import net.sparkmuse.user.UserFacade;

/**
 * @author neteller
 * @created: Mar 8, 2011
 */
public class SendNotificationTask extends Task {

  public static String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";
  public static String NOTIFICATION_USERID = "NOTIFICATION_USERID";

  private final UserFacade userFacade;

  @Inject
  public SendNotificationTask(ObjectDatastore datastore, UserFacade userFacade) {
    super(datastore);
    this.userFacade = userFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    userFacade.addNotification(Long.parseLong(getParameter(NOTIFICATION_USERID)), getParameter(NOTIFICATION_MESSAGE));
    return null;
  }
}
