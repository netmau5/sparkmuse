package net.sparkmuse.task;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.user.UserFacade;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import play.Logger;

/**
 * @author neteller
 * @created: Mar 30, 2011
 */
public class AddUserIdsToActivityItemSummaryTransformationTask extends TransformationTask<Activity> {

  private final ObjectDatastore datastore;
  private final UserFacade userFacade;

  @Inject
  public AddUserIdsToActivityItemSummaryTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, UserFacade userFacade) {
    super(cache, batchService, datastore);
    this.userFacade = userFacade;
    this.datastore = datastore;
  }

  protected Activity transform(Activity activity) {
    UserProfile userProfile = userFacade.getUserProfile(activity.getSummary().getUserName());
    if (null == userProfile) {
      Logger.error("Could not find user profile for user name [" + activity.getSummary().getUserName() + "]");
    }
    else if (null == userProfile.getUser()) {
      Logger.error("User for user name [" + activity.getSummary().getUserName() + "] does not exist!");
    }
    else {
      activity.getSummary().setUpdateAuthor(userProfile.getUser());
    }
    return activity;
  }

  protected FindCommand.RootFindCommand<Activity> find(boolean isNew) {
    return datastore.find().type(Activity.class);
  }
}
