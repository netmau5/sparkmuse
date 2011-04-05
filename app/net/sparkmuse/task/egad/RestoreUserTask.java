package net.sparkmuse.task.egad;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.twig.DatastoreService;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.task.TransformationTask;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;

import play.Logger;

/**
 * @author neteller
 * @created: Apr 3, 2011
 */
public class RestoreUserTask extends TransformationTask<Activity> {

  private static final String CACHE_KEY_CORRECT_USER_COUNTER = "CACHE_KEY_CORRECT_USER_COUNTER";

  private final ObjectDatastore datastore;
  private final DatastoreService datastoreService;
  private final Cache cache;

  @Inject
  public RestoreUserTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, DatastoreService datastoreService) {
    super(cache, batchService, datastore);
    this.datastoreService = datastoreService;
    this.datastore = datastore;
    this.cache = cache;
  }

  protected Activity transform(final Activity activity) {
    if (null == activity.getSummary().getUpdateAuthor()) {
      Logger.error("Activity does not have an associated user [" + activity.getSummary().getUserName() + "] for Activity [" + activity + "].");
      return null;
    }

    incrementCorrectCount();

    return null;
  }

  private void incrementCorrectCount() {
    Integer count = cache.get(CACHE_KEY_CORRECT_USER_COUNTER, Integer.class);
    if (null == count) {
      cache.set(CACHE_KEY_CORRECT_USER_COUNTER, new Integer(1));
    }
    else {
      cache.set(CACHE_KEY_CORRECT_USER_COUNTER, new Integer(count + 1));
    }
  }

  @Override
  protected void onBegin() {
    cache.delete(CACHE_KEY_CORRECT_USER_COUNTER);
  }

  @Override
  protected void onEnd() {
    Logger.info("[" + cache.get(CACHE_KEY_CORRECT_USER_COUNTER, Integer.class) + "] Users were found in the correct state.");
  }

  protected FindCommand.RootFindCommand<Activity> find(boolean isNew) {
    return datastore.find().type(Activity.class)
        .addFilter("population", Query.FilterOperator.EQUAL, Activity.Population.EVERYONE.toString())
        .addFilter("kind", Query.FilterOperator.EQUAL, Activity.Kind.POST.toString());
  }


}
