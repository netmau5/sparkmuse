package net.sparkmuse.task.mailing;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.Mailing;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.task.TransformationTask;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.mail.MailService;
import net.sparkmuse.user.UserFacade;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import org.joda.time.Days;
import org.joda.time.DateTime;
import org.joda.time.DateMidnight;

/**
 * @author neteller
 * @created: Mar 24, 2011
 */
public class SendMailingsTransformationTask extends TransformationTask<UserVO> {

  private static String CACHE_KEY_MAILINGS = SendMailingsTransformationTask.class.getName() + "|Mailings";

  private final IssueTaskService issueTaskService;
  private final MailService mailService;
  private final ObjectDatastore datastore;
  private final Cache cache;
  private final UserFacade userFacade;

  @Inject
  public SendMailingsTransformationTask(MailService mailService,
                                        IssueTaskService issueTaskService,
                                        UserFacade userFacade,
                                        Cache cache,
                                        BatchDatastoreService batchService,
                                        ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
    this.mailService = mailService;
    this.issueTaskService = issueTaskService;
    this.cache = cache;
    this.userFacade = userFacade;
  }

  private List<Mailing> getMailings() {
    List<Mailing> mailings = cache.get(CACHE_KEY_MAILINGS, List.class);

    if (null == mailings) {
      DateTime currentDate = new DateTime();
      DateMidnight midnight = new DateMidnight(currentDate.year().get(), currentDate.monthOfYear().get(), currentDate.dayOfMonth().get());
      mailings = mailService.mailingsFor(new DateTime(midnight.plusDays(1)));
      cache.set(CACHE_KEY_MAILINGS, mailings);
    }

    return mailings;
  }

  protected UserVO transform(UserVO user) {
    UserProfile profile = userFacade.findUserProfileBy(user.getId());

    if (profile.hasEmail() && user.isUser()) {
      for (Mailing mailing: getMailings()) {
        issueTaskService.issue(SendMailingToUserTask.class, ImmutableMap.<String, Object>of(
            SendMailingToUserTask.PARAMETER_MAILING_ID, mailing.getId(),
            SendMailingToUserTask.PARAMETER_USER_ID, user.getId()
        ), null);
      }
    }

    return user;
  }

  protected FindCommand.RootFindCommand<UserVO> find(boolean isNew) {
    return datastore.find().type(UserVO.class);
  }

}
