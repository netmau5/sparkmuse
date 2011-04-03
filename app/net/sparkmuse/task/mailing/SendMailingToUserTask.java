package net.sparkmuse.task.mailing;

import net.sparkmuse.task.Task;
import net.sparkmuse.mail.MailFacade;
import net.sparkmuse.user.UserFacade;
import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;
import com.google.code.twig.ObjectDatastore;

/**
 * @author neteller
 * @created: Mar 24, 2011
 */
public class SendMailingToUserTask extends Task {

  public static final String PARAMETER_MAILING_ID = "PARAMETER_MAILING_ID";
  public static final String PARAMETER_USER_ID = "PARAMETER_USER_ID";

  private final MailFacade mailFacade;
  private final UserFacade userFacade;

  @Inject
  public SendMailingToUserTask(ObjectDatastore datastore, MailFacade mailFacade, UserFacade userFacade) {
    super(datastore);
    this.mailFacade = mailFacade;
    this.userFacade = userFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    mailFacade.sendMailing(
        Long.parseLong(getParameter(PARAMETER_MAILING_ID)),
        userFacade.findUserProfileBy(Long.parseLong(getParameter(PARAMETER_USER_ID))));
    return null;
  }
}
