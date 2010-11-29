package net.sparkmuse.data.twig;

import net.sparkmuse.data.UserDao;
import net.sparkmuse.user.Votable;
import net.sparkmuse.user.Votables;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.UserVote;
import com.google.inject.Inject;
import models.UserModel;
import models.UserApplicationModel;
import models.VoteModel;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import com.google.common.collect.Sets;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.Set;
import java.util.Map;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

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
    return helper.update(user);
  }

  public void saveApplication(String userName, String url) {
    UserApplicationModel model = new UserApplicationModel();
    model.userName = userName;
    model.url = url;
    datastore.store(model);
  }

  /**
   * Stores a record of the vote for the given user, upvotes the votable, stores
   * it to the datastore, and adjusts the author's reputation.
   *
   * @param votable
   * @param voter
   */
  public void vote(Votable votable, UserVO voter) {
    final UserModel voterUM = map.fromEntity(voter).to(UserModel.class);
    datastore.associate(voterUM);

    final VoteModel voteModel = datastore.load()
        .type(VoteModel.class)
        .id(Votables.newKey(votable))
        .parent(voterUM)
        .now();

    //check for existing vote
    if (null == voteModel) {
      //store vote later so we can check if user has voted on whatever
      datastore.store().instance(VoteModel.newUpVote(votable, voter)).parent(voterUM).later();

      //record aggregate vote count on entity
      if (votable instanceof Entity) {
        votable.upVote();
        helper.update((Entity) votable);
      }

      //adjust reputation
      final UserVO author = votable.getAuthor();
      author.setReputation(author.getReputation() + 1);
      helper.update(author);
    }
  }

  public <T extends Entity<T>> void vote(Class<T> entityClass, Long id, UserVO voter) {
    T entity = helper.load(entityClass, id);
    if (entity instanceof Votable) {
      vote((Votable) entity, voter);
    }
  }

  public <T extends Entity & Votable> Set<UserVote> findVotesFor(Set<T> votables, UserVO user) {
    if (CollectionUtils.size(votables) == 0) return Sets.newHashSet();

    Set<String> ids = Sets.newHashSet();
    for (Votable votable: votables) {
      ids.add(Votables.newKey(votable));
    }

    final UserModel owner = map.fromEntity(user).to(UserModel.class);
    datastore.associate(owner);
    final Map<String, VoteModel> voteMap = datastore.load()
        .type(VoteModel.class)
        .ids(ids)
        .parent(owner)
        .now();

    //filter out nulls
    final Iterable<VoteModel> votes = Iterables.filter(voteMap.values(), new Predicate<VoteModel>(){
      public boolean apply(VoteModel voteModel) {
        return null != voteModel;
      }
    });

    return Sets.newHashSet(Iterables.transform(votes, new Function<VoteModel, UserVote>(){
      public UserVote apply(VoteModel voteModel) {
        final UserVote uv = new UserVote();
        uv.setKey(voteModel.id);
        uv.setVoteWeight(voteModel.voteWeight);
        return uv;
      }
    }));
  }

}
