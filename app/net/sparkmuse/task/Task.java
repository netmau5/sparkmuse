package net.sparkmuse.task;

import net.sparkmuse.data.entity.Migration;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Query;
import com.google.inject.internal.Nullable;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import play.Logger;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * @author neteller
 * @created: Feb 11, 2011
 */
public abstract class Task {

  private final ObjectDatastore datastore;
  private Cursor lastCursor;
  private final Map<String, String> parameters;

  public Task(ObjectDatastore datastore) {
    this.datastore = datastore;
    this.parameters = Maps.newHashMap();
  }

  public void addParameters(Map<String, String> parameters) {
    this.parameters.putAll(parameters);
  }

  protected String getParameter(String name) {
    return this.parameters.get(name);
  }

  public boolean isComplete() {
    return lastCursor == null;
  }

  public String getTaskName() {
    return this.getClass().getName();
  }

  /**
   * Called when task begins for any initialization logic.
   */
  protected void onBegin() {}

  /**
   * Called when task ends (no cursor)
   */
  protected void onEnd() {}

  public final Cursor execute(@Nullable Cursor cursor) {
    if (null == cursor) {
      onBegin();
      storeBegin();
    }

    try {
      lastCursor = runTask(cursor);
    } catch (Throwable e) {
      storeError(e);
      throw new RuntimeException(e);
    }

    if (isComplete()) {
      onEnd();
      storeEnd();
    }
    return lastCursor;
  }

  protected abstract Cursor runTask(@Nullable Cursor cursor);

  private Migration storeBegin() {
    Logger.info("Beginning task [" + this.getClass() + "].");

    final Migration migration = new Migration(getTaskName(), Migration.State.STARTED);

    datastore.store(migration);

    return migration;
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
    List<Migration> migrationList = datastore.find().type(Migration.class)
        .addFilter("state", Query.FilterOperator.EQUAL, Migration.State.STARTED.toString())
        .addFilter("taskName", Query.FilterOperator.EQUAL, getTaskName())
        .addSort("started", Query.SortDirection.DESCENDING)
        .fetchMaximum(1)
        .returnAll()
        .now();

    //if we fail, the current migration is turned into an error. if so, we need to recreate current migration for retry attempts
    if (Iterables.isEmpty(migrationList)) {
      return storeBegin();
    }
    else {
      return migrationList.get(0);
    }
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
