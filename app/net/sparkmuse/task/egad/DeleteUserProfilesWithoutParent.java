package net.sparkmuse.task.egad;

import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.task.TransformationTask;
import net.sparkmuse.common.Cache;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import play.Logger;

/**
 * @author neteller
 * @created: Apr 3, 2011
 */
public class DeleteUserProfilesWithoutParent extends TransformationTask<UserProfile> {

  private final ObjectDatastore datastore;

  @Inject
  public DeleteUserProfilesWithoutParent(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
  }

  protected UserProfile transform(UserProfile userProfile) {
    if (null == userProfile.getUser()) {
      Logger.error("User profile is orphaned from user [" + userProfile + "]");
      datastore.delete(userProfile);
    }

    return null;
  }

  protected FindCommand.RootFindCommand<UserProfile> find(boolean isNew) {
    return datastore.find().type(UserProfile.class);
  }
}
