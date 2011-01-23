package net.sparkmuse.task;

import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.common.Cache;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;

import java.lang.reflect.Method;

/**
 * Base class for tasks.
 *
 * @author neteller
 * @created: Jan 22, 2011
 */
public abstract class Task<T extends Entity> {

  private final Cache cache;
  private final BatchDatastoreService batchService;
  private Cursor lastCursor;

  public Task(Cache cache, BatchDatastoreService batchService) {
    this.cache = cache;
    this.batchService = batchService;
  }

  public boolean isComplete() {
    return lastCursor == null;
  }

  public abstract T transform(T t);

  public Cursor execute(@Nullable Cursor cursor) {
    lastCursor = batchService.transform(createTransformer(), determineTransformedClass(), cursor);
    return lastCursor;
  }

  private Class<T> determineTransformedClass() {
    try {
      final Method method = this.getClass().getMethod("transform", Entity.class);
      return (Class<T>) method.getParameterTypes()[0];
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Tranform method could not be found.", e);
    }
  }

  private Function<T, T> createTransformer() {
    return new Function<T, T>() {
      public T apply(T t) {
        final T entity = transform(t);
        cache.delete(entity);
        return entity;
      }
    };
  }

}
