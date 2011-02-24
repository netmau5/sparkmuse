package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.common.collect.Lists;
import com.google.inject.Inject;


/**
 * @author neteller
 * @created: Feb 19, 2011
 */
public class DataIntegrityMultiTask extends MultiTask {

  @Inject
  public DataIntegrityMultiTask(ObjectDatastore datastore, IssueTaskService issueTaskService) {
    super(datastore, issueTaskService);
  }

  protected Iterable<Class> getTasks() {
    return Lists.<Class>newArrayList(
        UpdateUserStatisticsTransformationTask.class,
        UserProfileRepairTransformationTask.class,
        UpdateSparkRatingsTransformationTask.class,
        PostCountRepairTransformationTask.class
    );
  }
}
