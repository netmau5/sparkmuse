package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.common.collect.Lists;
import com.google.appengine.api.datastore.Query;
import net.sparkmuse.data.entity.Migration;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.activity.ActivityService;
import net.sparkmuse.common.Cache;

import java.util.List;

/**
 * @author neteller
 * @created: Feb 13, 2011
 */
public class ActivityUpdateMultiTask extends MultiTask {

  @Inject
  public ActivityUpdateMultiTask(ObjectDatastore datastore, IssueTaskService issueTaskService) {
    super(datastore, issueTaskService);
  }

  protected Iterable<Class> getTasks() {
    List<Class> tasks = Lists.<Class>newArrayList(
        LikedActivityTransformationTask.class,
        SparkActivityTransformationTask.class,
        PostActivityTransformationTask.class
    );
    return tasks;
  }

  

}
