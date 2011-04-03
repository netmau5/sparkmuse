package net.sparkmuse.task.mailing;

import net.sparkmuse.task.Task;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.mail.MailFacade;
import net.sparkmuse.data.entity.UserProfile;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import com.google.appengine.api.datastore.Cursor;

/**
 * @author neteller
 * @created: Mar 24, 2011
 */
public class SendTestMailingTask extends Task {

  public static final String PARAMETER_MAILING_ID = "PARAMETER_MAILING_ID";

  private final MailFacade mailFacade;
  private final UserFacade userFacade;

  @Inject
  public SendTestMailingTask(ObjectDatastore datastore, UserFacade userFacade, MailFacade mailFacade) {
    super(datastore);
    this.userFacade = userFacade;
    this.mailFacade = mailFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    UserProfile userProfile = userFacade.getUserProfile("sparkmuse");
    mailFacade.sendMailing(Long.parseLong(getParameter(PARAMETER_MAILING_ID)), userProfile);
    return null;
  }

}
