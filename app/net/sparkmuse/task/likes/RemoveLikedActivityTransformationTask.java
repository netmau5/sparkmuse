package net.sparkmuse.task.likes;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.task.TransformationTask;
import net.sparkmuse.common.Cache;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;

/**
 * @author neteller
 * @created: Mar 4, 2011
 */
public class RemoveLikedActivityTransformationTask extends TransformationTask<Activity> {

  private final ObjectDatastore datastore;

  @Inject
  public RemoveLikedActivityTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
  }

  protected Activity transform(Activity activity) {
    datastore.delete(activity);
    return null;
  }

  protected FindCommand.RootFindCommand<Activity> find(boolean isNew) {
    return datastore.find().type(Activity.class).addFilter("sources", Query.FilterOperator.EQUAL, Activity.Source.LIKE.toString());
  }
}
