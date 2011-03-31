package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import com.google.appengine.api.datastore.Cursor;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.data.entity.UserVO;

/**
 * @author neteller
 * @created: Mar 30, 2011
 */
public class IncrementUserStatisticTask extends Task {

  public static final String PARAMETER_STATISTIC_TYPE = "PARAMETER_STATISTIC_TYPE";
  public static final String PARAMETER_USER_ID = "PARAMETER_USER_ID";

  private final UserFacade userFacade;

  @Inject
  public IncrementUserStatisticTask(ObjectDatastore datastore, UserFacade userFacade) {
    super(datastore);
    this.userFacade = userFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    Long userId = Long.parseLong(getParameter(PARAMETER_USER_ID));
    UserVO.Statistic statistic = UserVO.Statistic.valueOf(getParameter(PARAMETER_STATISTIC_TYPE));
    userFacade.incrementStatisticFor(userId, statistic);
    return null;
  }

}
