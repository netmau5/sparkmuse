package net.sparkmuse.data.twig;

import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.common.TimedTransformer;
import com.google.common.base.Function;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;
import com.google.apphosting.api.DeadlineExceededException;
import com.google.apphosting.api.ApiProxy;
import play.Logger;

/**
 * @author neteller
 * @created: Jan 17, 2011
 */
public class BatchDatastoreService {

  private final ObjectDatastore datastore;

  @Inject
  public BatchDatastoreService(ObjectDatastore datastore) {
    this.datastore = datastore;
  }

  /**
   *
   * @param entityClass
   * @param transformation
   * @param cursor          serialized, websafe cursor; null if none
   * @param <U>
   * @return
   */
  public <U extends Entity<U>> Cursor transform(Class<U> entityClass, final Function<U, U> transformation, @Nullable final Cursor cursor) {
    final FindCommand.RootFindCommand<U> find = datastore.find().type(entityClass).fetchNextBy(200);
    return transform(find, transformation, cursor);
  }

  public <U extends Entity<U>> Cursor transform(FindCommand.RootFindCommand<U> find, final Function<U, U> transformation, @Nullable final Cursor cursor) {
    if (null != cursor) find.continueFrom(cursor);
    return execute(transformation, find);
  }

  /**
   * Executes a timed job that will process a transformation over a given entity.  The transformation
   * logic is limited at approximately 15 seconds.  If the job doesn't complete in the allowed time, a
   * serialized cursor will be returned so that the job can be restarted at the last position.
   *
   * @param transformer
   * @param find
   * @return serialized cursor
   */
  private <U extends Entity<U>> Cursor execute(final Function<U, U> transformer, final FindCommand.RootFindCommand<U> find) {
    final QueryResultIterator<U> iterator = find.now();

    final Function<U, U> transformFunction = new Function<U, U>() {
      public U apply(U u) {
        final U transformedEntity = transformer.apply(u);
        DatastoreUtils.associate(transformedEntity, datastore);
        datastore.update(transformedEntity);
        return transformedEntity;
      }
    };

    TimedTransformer timedTransformer = new TimedTransformer(transformFunction);
    try {
      timedTransformer.transform(iterator);
    } catch (DatastoreTimeoutException timeout) {
      Logger.warn(timeout, "Datastore timeout occured during batch tranformation.");
      return timedTransformer.cursorFromLastSuccessfulTransform();
    } catch (DeadlineExceededException deadline) {
      Logger.warn(deadline, "Deadline exceeded during batch tranformation.");
      return timedTransformer.cursorFromLastSuccessfulTransform();
    } catch (ApiProxy.ApiDeadlineExceededException deadline) {
      Logger.warn(deadline, "API deadline exceeded during batch tranformation.");
      return timedTransformer.cursorFromLastSuccessfulTransform();
    }
    

    if (iterator.hasNext()) return iterator.getCursor();
    else return null;
  }

}
