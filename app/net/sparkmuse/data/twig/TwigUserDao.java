package net.sparkmuse.data.twig;

import net.sparkmuse.data.UserDao;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.VoteVO;
import net.sparkmuse.data.mapper.ObjectMapper;
import com.google.inject.Inject;
import com.vercer.engine.persist.ObjectDatastore;
import models.UserModel;
import models.UserApplicationModel;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Sep 19, 2010
 */
public class TwigUserDao extends TwigDao implements UserDao {

  @Inject
  public TwigUserDao(ObjectDatastore datastore, ObjectMapper map) {
    super(datastore, map);
  }

  public UserVO findOrCreateUserBy(final String authProviderUserId, final String userName) {
    final UserVO userVO = helper.only(UserVO.class, datastore.find()
        .type(UserModel.class)
        .addFilter("userId", EQUAL, authProviderUserId)
        .addFilter("userName", EQUAL, userName));

    if (null == userVO) {
      UserVO newUser = UserVO.newUser(authProviderUserId, userName);
      return helper.store(newUser);
    }
    else {
      return userVO;
    }
  }

  public UserVO findUserBy(Long id) {
    return helper.load(UserVO.class, id);
  }

  public UserVO update(UserVO user) {
    final UserModel userModel = map.fromEntity(user).to(UserModel.class);
    datastore.associate(userModel);
    datastore.update(userModel);
    return user;
  }

  public void saveApplication(String userName, String url) {
    UserApplicationModel model = new UserApplicationModel();
    model.userName = userName;
    model.url = url;
    datastore.store(model);
  }

  public VoteVO saveVote(final VoteVO vote) {
    return helper.store(vote);
  }

}
