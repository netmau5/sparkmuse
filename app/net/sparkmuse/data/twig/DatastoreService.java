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
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.OwnedEntity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.Cache;
import net.sparkmuse.data.Cacheable;
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

    return usersMap;
  }

  //READ COMMANDS

  @Deprecated //("Inline after CacheDao call removed")
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
    final U u = map.fromModel(only(findCommand)).to(entityClass);
    return afterRead(u);
  }

  public final <U extends Entity<U>> U load(Class<U> entityClass, Long id) {
    final U u = map.fromModel(datastore.load(Entity.modelClassFor(entityClass), id)).to(entityClass);
    return afterRead(u);
  }

  public final <I, U extends Entity<U>> Map<I, U> loadAll(Class<U> entityClass, Set<I> ids, Entity... parents) {
    final LoadCommand.MultipleTypedLoadCommand command = datastore.load().type(Entity.modelClassFor(entityClass)).ids(ids);
    for (Entity parent: parents) {
      command.parent(map.fromEntity(parent).to(parent.getModelClass()));
    }

    final Map<I, Object> idsToModels = command.now();
    final Map<I, U> toReturn = Maps.transformValues(idsToModels, map.newModelToEntityFunction(entityClass));
    return afterRead(toReturn);
  }

  public final <T, U extends Entity<U>> List<U> all(final Class<U> entityClass, FindCommand.RootFindCommand<T> findCommand) {
    final QueryResultIterator<T> resultIterator = findCommand.now();
    final ArrayList<U> toReturn = Lists.newArrayList(Iterators.transform(resultIterator, map.newModelToEntityFunction(entityClass)));
    return afterRead(toReturn);
  }

  public <T> T afterRead(T object) {
    if (null == object) return null;
    else return afterRead(Lists.newArrayList(object)).get(0);
  }

  private <T> List<T> afterRead(List<T> objects) {
    final List<OwnedEntity> toMergeOwners = Lists.newArrayList();
    for (T object: objects) {
      if (object instanceof OwnedEntity) {
        toMergeOwners.add((OwnedEntity) object);
      }
      if (object instanceof SparkVO || object instanceof UserVO) {
        cache.set(((Cacheable) object).getKey().toString(), object, "30d");
      }
    }

    mergeOwnersFor(toMergeOwners);

    return objects;
  }

  //generified iterable so we preserve type (might be ordered List)
  private <T extends OwnedEntity, I extends Iterable<T>> I mergeOwnersFor(I entities) {
    final Map<T, Long> ownedEntitiesToOwnersMap = Maps.newHashMap();
    for (T entity: entities) {
      ownedEntitiesToOwnersMap.put(entity, OwnedEntity.asOwnerIds.apply(entity));
    }

    final Map<Long, UserVO> userMap = this.getUsers(Sets.newHashSet(ownedEntitiesToOwnersMap.values()));

    for (Map.Entry<T, Long> ownedEntityEntry: ownedEntitiesToOwnersMap.entrySet()) {
      ownedEntityEntry.getKey().setAuthor(userMap.get(ownedEntityEntry.getValue()));
    }

    return entities;
  }

  //CREATE/UPDATES

  public final <T, U extends Entity<U>> U store(U entity) {
    if (null == entity) return null;
    final T model = map.fromEntity(entity).to((Class<T>) entity.getModelClass());

    //set the key on the model object
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

    datastore.associate(model);
    datastore.update(model);

    return entity;
  }

  //OTHER

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
    final DatastoreService service = this;
    final Function<T, U> transformFunction = new Function<T, U>() {
      public U apply(T t) {
        final U entity = modelToEntity.apply(t);
        final U transformedEntity = transformer.apply(entity);
        service.update(transformedEntity);
        return transformedEntity;
      }
    };

    new TimedTransformer(transformFunction).transform(iterator);

    if (iterator.hasNext()) return iterator.getCursor().toWebSafeString();
    else return null;
  }
}
