package net.sparkmuse.data.twig;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.OwnedEntity;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.common.NullTo;
import net.sparkmuse.discussion.AbstractSparkSearchResponse;
import net.sparkmuse.discussion.RecentSparks;
import net.sparkmuse.discussion.MostDiscussedSparks;
import net.sparkmuse.discussion.PopularSparks;

import java.util.Map;
import java.util.List;
import java.util.Collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 30, 2010
 */
public class After {

  static <T extends Entity> T read(T object, DatastoreService service) {
    if (null == object) return null;
    else return read(Lists.newArrayList(object), service).get(0);
  }

  static <T extends Entity> List<T> read(List<T> entityList, DatastoreService service) {
    final List<OwnedEntity> toMergeOwners = Lists.newArrayList();
    entityList = NullTo.empty(entityList);

    for (T entity: entityList) {
      if (entity instanceof OwnedEntity) {
        toMergeOwners.add((OwnedEntity) entity);
      }
    }
    updateCache(entityList, service.getCache());

//    mergeOwnersFor(toMergeOwners, service);

    return entityList;
  }

  static <T extends Entity, Id> Map<Id, T> read(Map<Id, T> entityMap, DatastoreService service) {
    read(Lists.newArrayList(NullTo.empty(entityMap.values())), service);
    return entityMap;
  }

  static <T extends Entity> T write(T entity, DatastoreService service) {
    return updateCache(entity, service.getCache());
  }

  //generified iterable so we preserve type (might be ordered List)
  private static <T extends OwnedEntity, I extends Iterable<T>> I mergeOwnersFor(I entities, DatastoreService service) {
    final Map<T, Long> ownedEntitiesToOwnersMap = Maps.newHashMap();
    for (T entity: entities) {
      ownedEntitiesToOwnersMap.put(entity, OwnedEntity.asOwnerIds.apply(entity));
    }

    final Map<Long, UserVO> userMap = service.getUsers(Sets.newHashSet(ownedEntitiesToOwnersMap.values()));

    for (Map.Entry<T, Long> ownedEntityEntry: ownedEntitiesToOwnersMap.entrySet()) {
      ownedEntityEntry.getKey().setAuthor(userMap.get(ownedEntityEntry.getValue()));
    }

    return entities;
  }

  private static <T extends Entity> T updateCache(T entity, Cache cache) {
    if (null != entity) return updateCache(Lists.newArrayList(entity), cache).get(0);
    else return null;
  }

  private static <T extends Entity, I extends Iterable<T>> I updateCache(I entityList, Cache cache) {
    //optimize to minimize cache interaction
    boolean updatedSpark = false;
    RecentSparks recentSparks = null;
    MostDiscussedSparks mostDiscussedSparks = null;
    PopularSparks popularSparks = null;

    for (T entity: entityList) {
      if (entity instanceof SparkVO) {
        SparkVO s = (SparkVO) entity;
        if (null == recentSparks && !updatedSpark) recentSparks = cache.get(CacheKeyFactory.newRecentSparksKey());
        if (null == mostDiscussedSparks && !updatedSpark) mostDiscussedSparks = cache.get(CacheKeyFactory.newMostDiscussedSparksKey());
        if (null == popularSparks && !updatedSpark) popularSparks = cache.get(CacheKeyFactory.newPopularSparksKey());
        update(recentSparks, s);
        update(mostDiscussedSparks, s);
        update(popularSparks, s);
        updatedSpark = true;
      }
      if (entity instanceof SparkVO || entity instanceof UserVO) {
        cache.put(entity);
      }
    }

    if (updatedSpark) {
      if (null != recentSparks) cache.put(recentSparks);
      if (null != mostDiscussedSparks) cache.put(mostDiscussedSparks);
      if (null != popularSparks) cache.put(popularSparks);
    }

    return entityList;
  }

  private static void update(AbstractSparkSearchResponse sparkSearch, SparkVO spark) {
    if (null != sparkSearch) sparkSearch.update(spark); //if cache not yet initialized, dont need to update
  }


}
