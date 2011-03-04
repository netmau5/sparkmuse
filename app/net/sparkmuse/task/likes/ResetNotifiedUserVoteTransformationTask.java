package net.sparkmuse.task.likes;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.task.TransformationTask;
import net.sparkmuse.common.Cache;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;

/**
 * @author neteller
 * @created: Mar 4, 2011
 */
public class ResetNotifiedUserVoteTransformationTask extends TransformationTask<UserVote> {

  private final ObjectDatastore datastore;

  @Inject
  public ResetNotifiedUserVoteTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
  }

  protected UserVote transform(UserVote activity) {
    activity.setNotified(false);
    return activity;
  }

  protected FindCommand.RootFindCommand<UserVote> find(boolean isNew) {
    return datastore.find().type(UserVote.class);
  }
}

