package net.sparkmuse.data.twig;

import net.sparkmuse.data.CacheDao;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.data.entity.StoredCacheEntry;
import net.sparkmuse.common.CacheKey;
import models.CacheModel;

import java.io.IOException;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.vercer.engine.persist.ObjectDatastore;
import net.sparkmuse.data.mapper.ObjectMapper;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import com.google.appengine.api.datastore.QueryResultIterator;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Sep 20, 2010
 */
public class TwigCacheDao extends TwigDao implements CacheDao {

  @Inject
  public TwigCacheDao(ObjectDatastore datastore, ObjectMapper map) {
    super(datastore, map);
  }

  public <T> StoredCacheEntry save(Cacheable<T> cacheable) {
    CacheModel model = toModel(cacheable);
    datastore.storeOrUpdate(model);
    return toEntry(cacheable.getKey(), model);
  }

  public void save(String key, Object value) {
    datastore.storeOrUpdate(toModel(key, value));
  }

  public <T> StoredCacheEntry<T> find(CacheKey<T> cacheKey) {
    final CacheModel cacheModel = helper.only(datastore.find()
        .type(CacheModel.class)
        .addFilter("key", EQUAL, cacheKey.toString()));

    return toEntry(cacheKey, cacheModel);
  }

  private static <T> StoredCacheEntry toEntry(CacheKey<T> key, CacheModel model){
    if (null == model) return null;
    try {
      final T value = new org.codehaus.jackson.map.ObjectMapper().readValue(model.value, key.getImplementingClass());
      StoredCacheEntry<T> cacheEntry = new StoredCacheEntry<T>(key, value);
      return cacheEntry;
    } catch (IOException e) {
      throw new RuntimeException("Error deserializing stored json serialization into " + key.getImplementingClass());
    }
  }

  private static CacheModel toModel(String key, Object value) {
    final org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();
    Preconditions.checkState(
        objectMapper.canSerialize(value.getClass()),
        "Cacheable contains a value that could not be serialized into Json."
    );
    CacheModel model = new CacheModel();
    model.key = key;
    try {
      model.value = objectMapper.writeValueAsString(value);
      return model;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> CacheModel toModel(Cacheable<T> cacheable) {
    return toModel(cacheable.getKey().toString(), cacheable);
  }


}
