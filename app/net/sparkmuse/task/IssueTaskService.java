package net.sparkmuse.task;

import net.sparkmuse.data.Cacheable;

import java.util.Map;

import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;

/**
 * Service for issuing asynchronous tasks.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public interface IssueTaskService {

  /**
   * This method should invoke a task which stores an Entity in it's proper table
   * and anything else in the CacheModel table.
   *
   * @param cacheable
   */
  <T extends Cacheable<T>> void issueCachePersistTask(Cacheable<T> cacheable);

  void issue(String action, Map<String, Object> parameters);

  <T extends Task> void issue(Class<T> taskClass, @Nullable Cursor cursor);

  <T extends Task> void issue(Class<T> taskClass, Map<?, ?> parameters, @Nullable Cursor cursor);
}
