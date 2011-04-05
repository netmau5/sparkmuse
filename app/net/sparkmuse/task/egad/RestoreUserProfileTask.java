package net.sparkmuse.task.egad;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.common.Cache;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.task.TransformationTask;
import play.Logger;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 3, 2011
 */
public class RestoreUserProfileTask extends TransformationTask<Activity> {

  private final ObjectDatastore datastore;
  private final UserFacade userFacade;

  @Inject
  public RestoreUserProfileTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, UserFacade userFacade) {
    super(cache, batchService, datastore);
    this.userFacade = userFacade;
    this.datastore = datastore;
  }

  protected Activity transform(Activity activity) {
    List<UserVO> users = datastore.find().type(UserVO.class)
        .addFilter("userNameLowercase", Query.FilterOperator.EQUAL, activity.getSummary().getUserName().toLowerCase())
        .returnAll()
        .now();

    if (users.size() == 0) {
      Logger.error("Could not find user for activity [" + activity + "]");
    }
    else if (users.size() > 1) {
      Logger.error("Found more than one user for activity [" + activity + "] <" + users + ">");
    }
    else {
      UserVO user = users.get(0);
      List<UserProfile> userProfiles = datastore.find().type(UserProfile.class).ancestor(user).returnAll().now();
      if (userProfiles.size() == 0) {
        Logger.error("Could not find user profile for user [" + user + "]");
      }
      else if (userProfiles.size() > 1) {
        Logger.error("Found more than one user profile for user [" + user + "] <" + userProfiles + ">");
      }
    }

    return null;
  }

  protected FindCommand.RootFindCommand<Activity> find(boolean isNew) {
    return datastore.find().type(Activity.class);
  }

}
