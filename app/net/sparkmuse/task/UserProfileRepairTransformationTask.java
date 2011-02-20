package net.sparkmuse.task;

import net.sparkmuse.data.entity.UserVO;
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
 * @created: Feb 19, 2011
 */
public class UserProfileRepairTransformationTask extends TransformationTask<UserVO> {

  private final ObjectDatastore datastore;
  private final UserFacade userFacade;
  
  @Inject
  public UserProfileRepairTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, UserFacade userFacade) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
    this.userFacade = userFacade;
  }

  protected UserVO transform(UserVO userVO) {
    UserProfile userProfile = userFacade.getUserProfile(userVO.getUserName());
    if (null == userProfile) {
      Logger.error("UserProfile not found for user: " + userVO.getUserName());
      UserProfile newProfile = UserProfile.newProfile(userVO);
      datastore.store(newProfile);
    }

    return userVO;
  }

  protected FindCommand.RootFindCommand<UserVO> find(boolean isNew) {
    return datastore.find().type(UserVO.class);
  }
}
