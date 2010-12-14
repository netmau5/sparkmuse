package net.sparkmuse.data.twig;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.code.twig.LoadCommand;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Key;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.google.inject.Inject;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.TimedTransformer;
import net.sparkmuse.common.CacheKeyFactory;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 22, 2010
 */
public class DatastoreService {

  private final ObjectDatastore datastore;
  private final Cache cache;

  @Inject
  public DatastoreService(ObjectDatastore datastore, Cache cache) {
    this.datastore = datastore;
    this.cache = cache;
  }

  public ObjectDatastore getDatastore() {
    return this.datastore;
  }
  
  Cache getCache() {
    return cache;
  }

  /**
   * Attempts to get a user from the cache.  Failing that, it will lookup the user and add it to
   * the cache.
   *
   * @param id
   * @return
   */
  public UserVO getUser(final Long id) {
    UserVO cachedUser = cache.get(CacheKeyFactory.newUserKey(id));
    if (null != cachedUser) {
      return cachedUser;
    }

    final UserVO userVO = load(UserVO.class, id);
    if (null == userVO) return null;
    else return userVO;
  }

  /**
   * Attempts to get a user from the cache.  Failing that, it will lookup the user and add it to
   * the cache.
   *
   * @param userIds
   * @return
   */
  public Map<Long, UserVO> getUsers(final Set<Long> userIds) {
    Set<Long> toQuery = Sets.newHashSet();
    Set<UserVO> cachedUsers = Sets.newHashSet();
    for (Long userId: userIds) {
      UserVO cachedUser = cache.get(CacheKeyFactory.newUserKey(userId));
      if (null != cachedUser) {
        cachedUsers.add(cachedUser);
      }
      else {
        toQuery.add(userId);
      }
    }

    final Map<Long, UserVO> usersMap = CollectionUtils.size(toQuery) > 0 ? loadAll(UserVO.class, toQuery) : Maps.<Long, UserVO>newHashMap();
    usersMap.putAll(Maps.uniqueIndex(cachedUsers, UserVO.asUserIds));

    return usersMap;
  }

  //READ COMMANDS

  public final <T extends Entity<T>> T only(FindCommand.RootFindCommand<T> findCommand) {
    final QueryResultIterator<T> resultsIterator = findCommand.now();
    if (resultsIterator.hasNext()) {
      final T toReturn = resultsIterator.next();
      Preconditions.checkState(!resultsIterator.hasNext(), "Only one result requested but more than one returned.");
      return After.read(toReturn, this);

    }
    else return null;
  }

  public final <U extends Entity<U>> U load(Class<U> entityClass, Long id) {
    final U u = datastore.load(entityClass, id);
    return After.read(u, this);
  }

  public final <I, U extends Entity<U>> Map<I, U> loadAll(Class<U> entityClass, Set<I> ids, Entity... parents) {
    final LoadCommand.MultipleTypedLoadCommand command = datastore.load().type(entityClass).ids(ids);
    for (Entity parent: parents) {
      command.parent(parent);
    }

    final Map<I, U> idsToModels = command.now();
    return After.read(idsToModels, this);
  }

  public final <U extends Entity<U>> List<U> all(FindCommand.RootFindCommand<U> findCommand) {
    final QueryResultIterator<U> resultIterator = findCommand.now();
    final List<U> toReturn = Lists.newArrayList(resultIterator);
    return After.read(toReturn, this);
  }

  //CREATE/UPDATES

  public final <T, U extends Entity<U>> U store(U entity) {
    if (null == entity) return null;

    //set the key on the model object
    final Key key = datastore.store().instance(entity).now();
    entity.setId(key.getId());

    return After.write(entity, this);
  }

  public final <T, U extends Entity<U>> U update(U entity) {
    if (null == entity) return null;

    associate(entity);
    datastore.update(entity);

    return After.write(entity, this);
  }

  //OTHER

  public final <T, U extends Entity<U>> U associate(U entity) {
    if (null == entity) return entity;

    //this should set the key on the model object automatically
    if (datastore.associatedKey(entity) == null) datastore.associate(entity);
    return entity;
  }

  /**
   * Executes a timed job that will process a transformation over a given entity.  The transformation
   * logic is limited at approximately 15 seconds.  If the job doesn't complete in the allowed time, a
   * serialized cursor will be returned so that the job can be restarted at the last position.
   *
   * @param transformer
   * @param find
   * @return serialized cursor
   */
  public <U extends Entity<U>> String execute(final Function<U, U> transformer, final FindCommand.RootFindCommand<U> find) {
    final QueryResultIterator<U> iterator = find.now();

    final DatastoreService service = this;
    final Function<U, U> transformFunction = new Function<U, U>() {
      public U apply(U u) {
        final U transformedEntity = transformer.apply(u);
        service.update(transformedEntity);
        return transformedEntity;
      }
    };

    new TimedTransformer(transformFunction).transform(iterator);

    if (iterator.hasNext()) return iterator.getCursor().toWebSafeString();
    else return null;
  }
}
