package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import com.google.appengine.api.datastore.Cursor;
import net.sparkmuse.common.Cache;

/**
 * @author neteller
 * @created: Feb 18, 2011
 */
public class ResetCacheTask extends Task {

  private final Cache cache;

  @Inject
  public ResetCacheTask(ObjectDatastore datastore, Cache cache) {
    super(datastore);

    this.cache = cache;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    cache.clear();
    return null;
  }
}
