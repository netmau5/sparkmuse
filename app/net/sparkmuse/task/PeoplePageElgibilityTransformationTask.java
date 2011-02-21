package net.sparkmuse.task;

import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;
import org.apache.commons.lang.StringUtils;
import play.Play;

/**
 * @author neteller
 * @created: Feb 15, 2011
 */
public class PeoplePageElgibilityTransformationTask extends TransformationTask<UserProfile> {

  private static final int MIN_REP = Integer.parseInt(Play.configuration.getProperty("people-page.min-rep"));
  private final ObjectDatastore datastore;

  @Inject
  public PeoplePageElgibilityTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
  }

  protected UserProfile transform(UserProfile userProfile) {
    //update your profile
    if (StringUtils.isEmpty(userProfile.getBio()) && userProfile.getExpertises().isEmpty() && StringUtils.isEmpty(userProfile.getName())) {
      return userProfile;
    }

    //contribute to a Spark or create your own
    if (userProfile.getUser().getPosts() == 0 && userProfile.getUser().getSparks() == 0) {
      return userProfile;
    }

    //earn at least 10 rep
    if (userProfile.getUser().getReputation() < MIN_REP) {
      return userProfile;
    }

    userProfile.setPeopleElgible(true);
    return userProfile;
  }

  protected FindCommand.RootFindCommand<UserProfile> find(boolean isNew) {
    FindCommand.RootFindCommand<UserProfile> find = datastore.find().type(UserProfile.class);

    final Migration lastMigration = lastMigration();
    if (null != lastMigration) { //has a value set for people elgible
      find.addFilter("peopleElgible", Query.FilterOperator.EQUAL, Boolean.FALSE);
    }

    return find;
  }
}
