package net.sparkmuse.data.twig;

import net.sparkmuse.data.UserDao;
import net.sparkmuse.data.Votable;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.mapper.ObjectMapper;
import com.google.inject.Inject;
import models.UserModel;
import models.UserApplicationModel;
import models.VoteModel;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import com.google.code.twig.ObjectDatastore;

import java.util.Set;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Sep 19, 2010
 */
public class TwigUserDao extends TwigDao implements UserDao {

  @Inject
  public TwigUserDao(DatastoreService service) {
    super(service);
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
    return helper.getUser(id);
  }

  public Map<Long, UserVO> findUsersBy(Set<Long> ids) {
    return helper.getUsers(ids);
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

  public void vote(Votable votable, UserVO voter) {
    //store vote so we can later determine if a user has voted on an entity
    final UserModel voterUM = map.fromEntity(voter).to(UserModel.class);
    datastore.associate(voterUM);
    datastore.store().instance(newVoteModel(votable)).parent(voterUM).later();

    //record aggregate vote count on entity
    if (votable instanceof Entity) {
      votable.upVote();
      helper.store((Entity) votable);
    }

    //adjust reputation
    final UserVO author = votable.getAuthor();
    helper.associate(author);
    author.setReputation(author.getReputation() + 1);
    helper.store(author);
  }

  private static VoteModel newVoteModel(Votable votable) {
    final VoteModel vm = new VoteModel();
    final String className = vm.entityClassName = votable.getClass().getName();
    final Long id = vm.entityId = votable.getId();
    vm.key = className + "|" + id;
    vm.voteWeight = 1;
    return vm;
  }

}
