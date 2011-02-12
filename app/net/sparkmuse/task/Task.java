package net.sparkmuse.task;

import net.sparkmuse.data.entity.Migration;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Query;
import com.google.inject.internal.Nullable;
import com.google.common.base.Joiner;
import play.Logger;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author neteller
 * @created: Feb 11, 2011
 */
public abstract class Task {

  private final ObjectDatastore datastore;
  private Cursor lastCursor;

  public Task(ObjectDatastore datastore) {
    this.datastore = datastore;
  }

  public boolean isComplete() {
    return lastCursor == null;
  }

  protected abstract String getTaskName();


  public final Cursor execute(@Nullable Cursor cursor) {
    if (null == cursor) storeBegin();

    try {
      lastCursor = runTask(cursor);
    } catch (Throwable e) {
      storeError(e);
      throw new RuntimeException(e);
    }

    if (isComplete()) storeEnd();
    return lastCursor;
  }

  protected abstract Cursor runTask(@Nullable Cursor cursor);

  private void storeBegin() {
    Logger.info("Beginning task [" + this.getClass() + "].");

    final Migration migration = new Migration(getTaskName(), Migration.State.STARTED);

    datastore.store(migration);
  }

  private void storeEnd() {
    Logger.info("Completed task [" + this.getClass() + "].");

    final Migration migration = currentMigration();
    migration.setEnded(new DateTime());
    migration.setState(Migration.State.COMPLETED);

    datastore.update(migration);
  }

  private void storeError(Throwable e) {
    Logger.info("Error in task [" + this.getClass() + "].");

    final Migration migration = currentMigration();
    migration.setEnded(new DateTime());
    migration.setState(Migration.State.ERROR);
    migration.setError(e.getClass() + "\n" + e.getMessage() + "\n" + Joiner.on("\n").join(e.getStackTrace()));

    datastore.update(migration);
  }

  protected Migration currentMigration() {
    return datastore.find().type(Migration.class)
        .addFilter("state", Query.FilterOperator.EQUAL, Migration.State.STARTED.toString())
        .addFilter("taskName", Query.FilterOperator.EQUAL, getTaskName())
        .addSort("started", Query.SortDirection.DESCENDING)
        .fetchMaximum(1)
        .returnAll()
        .now()
        .get(0);
  }

  protected Migration lastMigration() {
    final List<Migration> migrationList = datastore.find().type(Migration.class)
        .addFilter("state", Query.FilterOperator.EQUAL, Migration.State.COMPLETED.toString())
        .addFilter("taskName", Query.FilterOperator.EQUAL, getTaskName())
        .addSort("started", Query.SortDirection.DESCENDING)
        .fetchMaximum(1)
        .returnAll()
        .now();
    return migrationList.size() > 0 ? migrationList.get(0) : null;
  }

}
