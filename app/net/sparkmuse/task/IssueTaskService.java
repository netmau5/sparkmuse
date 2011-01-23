package net.sparkmuse.task;

import net.sparkmuse.data.Cacheable;

import java.util.Map;

import com.google.appengine.api.datastore.Cursor;

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

  /**
   * Invokes a task to update spark ratings from the given cursor.  This should not
   * be called without a cursor as the first job should be initiated via cron.  If the
   * first job doesn't complete, issue a task to complete it here.
   *
   * @param cursor
   */
  void issueSparkRatingUpdate(String cursor);

  /**
   * Invokes a task to repair post counts on Sparks.
   *
   * @param cursor
   */
  void issuePostCountRepairer(String cursor);

  void issue(String action, Map<String, Object> parameters);

  <T extends Task> void issue(Class<T> taskClass, Cursor cursor);
}
