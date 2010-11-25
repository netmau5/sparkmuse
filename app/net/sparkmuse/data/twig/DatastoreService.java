package net.sparkmuse.data.twig;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Key;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.google.inject.Inject;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.OwnedEntity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.Cache;
import net.sparkmuse.common.TimedTransformer;
import net.sparkmuse.common.CacheKeyFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import org.apache.commons.collections.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 22, 2010
 */
public class DatastoreService {

  private final ObjectDatastore datastore;
  private final ObjectMapper map;
  private final Cache cache;

  @Inject
  public DatastoreService(ObjectDatastore datastore, ObjectMapper mapper, Cache cache) {
    this.datastore = datastore;
    this.map = mapper;
    this.cache = cache;
  }

  public ObjectDatastore getDatastore() {
    return this.datastore;
  }

  public ObjectMapper getMapper() {
    return this.map;
  }

  /**
   * Attempts to get a user from the cache.  Failing that, it will lookup the user and add it to
   * the cache.
   *
   * @param id
   * @return
   */
  public UserVO getUser(final Long id) {
    UserVO cachedUser = cache.get(CacheKeyFactory.newUserKey(id).toString(), UserVO.class);
    if (null != cachedUser) {
      return cachedUser;
    }

    final UserVO userVO = load(UserVO.class, id);
    cache.add(userVO.getKey().toString(), userVO, "30d");
    return userVO;
  }

  /**
   * Attempts to get a user from the cache.  Failing that, it will lookup the user and add it to
   * the cache.
   *
   * @param userIds
   * @return
   */
  public Map<Long, UserVO> getUsers(final Iterable<Long> userIds) {
    Set<Long> toQuery = Sets.newHashSet();
    Set<UserVO> cachedUsers = Sets.newHashSet();
    for (Long userId: userIds) {
      UserVO cachedUser = cache.get(CacheKeyFactory.newUserKey(userId).toString(), UserVO.class);
      if (null != cachedUser) {
        cachedUsers.add(cachedUser);
      }
      else {
        toQuery.add(userId);
      }
    }

    final Map<Long, UserVO> usersMap = CollectionUtils.size(toQuery) > 0 ? loadAll(UserVO.class, toQuery) : Maps.<Long, UserVO>newHashMap();
    usersMap.putAll(Maps.uniqueIndex(cachedUsers, UserVO.asUserIds));

    //@todo put users in cache

    return usersMap;
  }

  //generified iterable so we preserve type (might be ordered List)
  public <T extends OwnedEntity, I extends Iterable<T>> I mergeOwnersFor(I entities) {
    final HashMap<T, Long> ownedEntitiesToOwnersMap = Maps.newHashMap();
    for (T entity: entities) {
      ownedEntitiesToOwnersMap.put(entity, OwnedEntity.asOwnerIds.apply(entity));
    }

    final Map<Long, UserVO> userMap = this.getUsers(ownedEntitiesToOwnersMap.values());

    for (Map.Entry<T, Long> ownedEntityEntry: ownedEntitiesToOwnersMap.entrySet()) {
      ownedEntityEntry.getKey().setAuthor(userMap.get(ownedEntityEntry.getValue()));
    }

    return entities;
  }

  public <T extends OwnedEntity> T mergeOwnerFor(T ownedEntity) {
    final Iterable<T> iterable = mergeOwnersFor(Lists.newArrayList(ownedEntity));
    return Iterables.size(iterable) > 0 ? iterable.iterator().next() : null;
  }

  public final <T> T only(FindCommand.RootFindCommand<T> findCommand) {
    final QueryResultIterator<T> resultsIterator = findCommand.now();
    if (resultsIterator.hasNext()) {
      final T toReturn = resultsIterator.next();
      Preconditions.checkState(!resultsIterator.hasNext(), "Only one result requested but more than one returned.");
      return toReturn;
    }
    else return null;
  }

  public final <T, U extends Entity<U>> U only(Class<U> entityClass, FindCommand.RootFindCommand<T> findCommand) {
    return map.fromModel(only(findCommand)).to(entityClass);
  }

  public final <U extends Entity<U>> U load(Class<U> entityClass, Long id) {
    return map.fromModel(datastore.load(Entity.modelClassFor(entityClass), id)).to(entityClass);
  }

  public final <U extends Entity<U>> Map<Long, U> loadAll(Class<U> entityClass, Set<Long> ids) {
    final Map<Long, U> idsToModels = datastore.loadAll(Entity.modelClassFor(entityClass), ids);
    return Maps.transformValues(idsToModels, map.newModelToEntityFunction(entityClass));
  }

  public final <T, U extends Entity<U>> List<U> all(final Class<U> entityClass, FindCommand.RootFindCommand<T> findCommand) {
    final QueryResultIterator<T> resultIterator = findCommand.now();
    return Lists.newArrayList(Iterators.transform(resultIterator, map.newModelToEntityFunction(entityClass)));
  }

  public final <T, U extends Entity<U>> U store(U entity) {
    if (null == entity) return null;
    final T model = map.fromEntity(entity).to((Class<T>) entity.getModelClass());

    //this should set the key on the model object automatically
    final Key key = datastore.store().instance(model).now();
    entity.setId(key.getId());

    return entity;
  }

  public final <T, U extends Entity<U>> void storeLater(U entity) {
    if (null == entity) return;
    final T model = map.fromEntity(entity).to((Class<T>) entity.getModelClass());

    datastore.store().instance(model).later();
  }

  public final <T, U extends Entity<U>> U update(U entity) {
    if (null == entity) return null;
    final T model = map.fromEntity(entity).to((Class<T>) entity.getModelClass());

    datastore.update(model);

    return entity;
  }

  public final <T, U extends Entity<U>> void associate(U entity) {
    if (null == entity) return;
    final T model = map.fromEntity(entity).to((Class<T>) entity.getModelClass());

    //this should set the key on the model object automatically
    datastore.associate(model);
  }

  /**
   * Executes a timed job that will process a transformation over a given entity.  The transformation
   * logic is limited at approximately 15 seconds.  If the job doesn't complete in the allowed time, a
   * serialized cursor will be returned so that the job can be restarted at the last position.
   *
   * @param entityClass
   * @param transformer
   * @param find
   * @return serialized cursor
   */
  public <T, U extends Entity<U>> String execute(final Class<U> entityClass,
                                                 final Function<U, U> transformer,
                                                 final FindCommand.RootFindCommand<T> find) {
    final QueryResultIterator<T> iterator = find.now();

    final Function<T, U> modelToEntity = map.newModelToEntityFunction(entityClass);
    final Function<U, T> entityToModel = map.newEntityToModelFunction(Entity.modelClassFor(entityClass));

    final Function<T, T> transformFunction = new Function<T, T>() {
      public T apply(T t) {
        final U entity = modelToEntity.apply(t);
        final U transformedEntity = transformer.apply(entity);
        final T model = entityToModel.apply(transformedEntity);
        datastore.update(model);
        return model;
      }
    };

    new TimedTransformer(transformFunction).transform(iterator);

    if (iterator.hasNext()) return iterator.getCursor().toWebSafeString();
    else return null;
  }
}
