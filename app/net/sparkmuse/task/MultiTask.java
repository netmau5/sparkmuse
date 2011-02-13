package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;
import com.google.common.base.Preconditions;

/**
 * Runs multiple tasks
 *
 * @author neteller
 * @created: Feb 12, 2011
 */
public abstract class MultiTask extends Task {

  private final IssueTaskService issueTaskService;

  @Inject
  public MultiTask(ObjectDatastore datastore, IssueTaskService issueTaskService) {
    super(datastore);
    this.issueTaskService = issueTaskService;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    for (Class<? extends Task> clazz: getTasks()) {
      Preconditions.checkState(Task.class.isAssignableFrom(clazz));
      issueTaskService.issue(clazz, null);
    }

    return null;
  }

  protected abstract Iterable<Class> getTasks();

}
