package net.sparkmuse.task;

import com.google.inject.Inject;
import com.google.common.base.Function;
import com.google.code.twig.ObjectDatastore;
import net.sparkmuse.data.WriteThruCacheService;
import net.sparkmuse.data.CacheDao;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.mapper.ObjectMapper;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 16, 2010
 */
public class PersistCacheValueTaskHandler<E extends Entity> implements Function<String, Boolean> {

  private final ObjectDatastore datastore;
  private final WriteThruCacheService writeThruCacheService;
  private final CacheDao cacheDao;
  private final ObjectMapper map;

  @Inject
  public PersistCacheValueTaskHandler(final ObjectDatastore datastore,
                                      final WriteThruCacheService writeThruCacheService,
                                      final CacheDao cacheDao,
                                      final ObjectMapper mapper){
    this.datastore = datastore;
    this.writeThruCacheService = writeThruCacheService;
    this.cacheDao = cacheDao;
    this.map = mapper;
  }

  public Boolean apply(String key) {
    final Object o = writeThruCacheService.get(key);

    if (o instanceof Entity) {
      final E e = (E) o;
      final Class modelClass = e.getModelClass();
      final Object model = map.fromEntity(e).to(modelClass);
      if (null == e.getId()) {
        datastore.store(model);
      }
      else {
        datastore.associate(model);
        datastore.update(model);
      }
    }
    else {
      cacheDao.save(key, o);
    }

    return true;
  }
}
