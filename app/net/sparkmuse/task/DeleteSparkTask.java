package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import com.google.appengine.api.datastore.Cursor;
import net.sparkmuse.discussion.SparkFacade;

/**
 * @author neteller
 * @created: Mar 4, 2011
 */
public class DeleteSparkTask extends Task {

  public static final String PARM_SPARKID = "SparkId";

  private final SparkFacade sparkFacade;

  @Inject
  public DeleteSparkTask(ObjectDatastore datastore, SparkFacade sparkFacade) {
    super(datastore);
    this.sparkFacade = sparkFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    sparkFacade.deleteSpark(Long.parseLong(getParameter(PARM_SPARKID)));
    return null;
  }
}
