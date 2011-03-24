package net.sparkmuse.task.mailing;

import net.sparkmuse.task.Task;
import net.sparkmuse.mail.MailService;
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

  public static String PARAMETER_MAILING_ID;
  public static String PARAMETER_USER_ID;

  private final MailService mailService;
  private final UserFacade userFacade;

  @Inject
  public SendMailingToUserTask(ObjectDatastore datastore, MailService mailService, UserFacade userFacade) {
    super(datastore);
    this.mailService = mailService;
    this.userFacade = userFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    mailService.sendMailing(
        Long.parseLong(getParameter(PARAMETER_MAILING_ID)),
        userFacade.findUserProfileBy(Long.parseLong(getParameter(PARAMETER_USER_ID))));
    return null;
  }
}
