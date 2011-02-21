package net.sparkmuse.task;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.UserApplication;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.common.Cache;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.appengine.api.datastore.Query;
import com.google.inject.Inject;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import play.Logger;

/**
 * @author neteller
 * @created: Feb 20, 2011
 */
public class UserApplicationCleanupTransformationTask extends TransformationTask<UserVO> {

  private final ObjectDatastore datastore;

  @Inject
  public UserApplicationCleanupTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
  }

  protected UserVO transform(UserVO userVO) {
    List<UserApplication> apps = findApplicationsFor(userVO);
    if (CollectionUtils.size(apps) > 0) {
      Logger.warn("User [" + userVO.getUserName() + "] has already been granted membership but still has [" + apps.size() + "] applications pending.");
      datastore.deleteAll(apps);
    }

    return userVO;
  }

  private final List<UserApplication> findApplicationsFor(UserVO user){
    return datastore.find().type(UserApplication.class)
        .addFilter("userName", Query.FilterOperator.EQUAL, "@" + user.getUserName())
        .returnAll()
        .now();
  }

  protected FindCommand.RootFindCommand<UserVO> find(boolean isNew) {
    return datastore.find().type(UserVO.class)
        .addFilter("accessLevel", Query.FilterOperator.EQUAL, AccessLevel.USER.toString());
  }
}
