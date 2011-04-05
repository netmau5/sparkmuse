package net.sparkmuse.task.egad;

import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;
import com.google.code.twig.ObjectDatastore;
import net.sparkmuse.task.Task;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.user.UserFacade;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 3, 2011
 */
public class RestoreEntitiesTask extends Task {

  private final ObjectDatastore datastore;
  private final UserFacade userFacade;

  @Inject
  public RestoreEntitiesTask(ObjectDatastore datastore, UserFacade userFacade) {
    super(datastore);
    this.datastore = datastore;
    this.userFacade = userFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    restoreUser(175001L, "beardygeek");
    restoreUser(216001L, "AutoPidgeon");
    restoreUser(197004L, "me_sawhney");
    restoreUser(271002L, "flyblackbox");

    return null;
  }

  private void restoreUser(Long id, String username) {
    UserVO user = datastore.load(UserVO.class, id);
    if (null != user) {
      datastore.delete(user);
    }
    UserProfile userProfile = userFacade.createUser(username);
    UserVO newUser = userProfile.getUser();
    datastore.delete(userProfile);
    datastore.delete(newUser);

    newUser.setAccessLevel(AccessLevel.USER);
    newUser.setFirstLogin(new DateTime());
    newUser.setId(id);
    datastore.store(newUser);

    List<UserProfile> userProfiles = datastore.find().type(UserProfile.class)
        .ancestor(newUser)
        .returnAll()
        .now();
    if (userProfiles.size() == 0) {
      userProfile.setUser(newUser);
      datastore.store(userProfile);
    }
  }
}
