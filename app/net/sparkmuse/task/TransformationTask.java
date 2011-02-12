package net.sparkmuse.task;

import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.common.Cache;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Query;
import com.google.inject.internal.Nullable;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;

import java.util.List;

import play.Logger;
import org.joda.time.DateTime;

/**
 * Base class for tasks.
 *
 * @author neteller
 * @created: Jan 22, 2011
 */
public abstract class TransformationTask<T extends Entity> extends Task {

  private final Cache cache;
  private final BatchDatastoreService batchService;

  public TransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(datastore);
    this.cache = cache;
    this.batchService = batchService;
  }

  protected abstract T transform(T t);

  protected abstract FindCommand.RootFindCommand<T> find(boolean isNew);

  protected Cursor runTask(@Nullable Cursor cursor) {
    return batchService.transform(find(null == cursor), createTransformer(), cursor);
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
