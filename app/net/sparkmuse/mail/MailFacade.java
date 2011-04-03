package net.sparkmuse.mail;

import net.sparkmuse.data.entity.Mailing;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.twig.DatastoreUtils;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.common.Templates;
import net.sparkmuse.activity.ActivityService;

import java.util.List;
import java.util.regex.Pattern;

import com.google.appengine.api.datastore.Query;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Apr 3, 2011
 */
public class MailFacade {

  private static final String TOKEN_TOP_CONTRIBUTORS = "${TOPCONTRIBUTORS}";

  private final ObjectDatastore datastore;
  private final ActivityService activityService;
  private final SendMailService sendMailService;

  @Inject
  public MailFacade(ObjectDatastore datastore, ActivityService activityService, SendMailService sendMailService) {
    this.datastore = datastore;
    this.activityService = activityService;
    this.sendMailService = sendMailService;
  }

  public Mailing save(Mailing newMailing) {
    newMailing = interpolate(newMailing);
    if (newMailing.getId() != null) {
      Mailing existingMailing = datastore.load(Mailing.class, newMailing.getId());
      newMailing = Reflections.overlay(existingMailing, newMailing);
    }
    return DatastoreUtils.storeOrUpdate(newMailing, datastore);
  }

  public List<Mailing> getAllMailings() {
    return datastore.find().type(Mailing.class)
        .addSort("created", Query.SortDirection.DESCENDING)
        .returnAll()
        .now();
  }

  public Mailing getMailingBy(Long id) {
    return datastore.load(Mailing.class, id);
  }

  public List<Mailing> mailingsFor(DateTime date) {
    return datastore.find().type(Mailing.class)
        .addFilter("sendDate", Query.FilterOperator.LESS_THAN, date.getMillis())
        .addFilter("isSent", Query.FilterOperator.EQUAL, false)
        .addSort("sendDate", Query.SortDirection.DESCENDING)
        .returnAll()
        .now();
  }

  public void sendMailing(Long mailingId, UserProfile userProfile) {
    if (userProfile.hasEmail()) {
      Mailing mailing = getMailingBy(mailingId);
      this.sendMailService.prepareAndSendMessage(new MailingTemplate(mailing, userProfile));
    }
  }

  public void markSent(List<Mailing> mailingList) {
    datastore.updateAll(Lists.newArrayList(Iterables.transform(mailingList, new Function<Mailing, Mailing>(){
      public Mailing apply(Mailing mailing) {
        mailing.setSent(true);
        DatastoreUtils.associate(mailing, datastore);
        return mailing;
      }
    })));
  }

  private Mailing interpolate(Mailing mailing) {
    if (mailing.getContent().contains(TOKEN_TOP_CONTRIBUTORS)) {
      List<UserVO> topContributors = activityService.determineTopContributors();
      String topContributorsPartial = Templates.render(new TopContributorsTemplate(topContributors));
      mailing.setContent(mailing.getContent().replace(TOKEN_TOP_CONTRIBUTORS, topContributorsPartial));
    }
    return mailing;
  }

}
