package net.sparkmuse.task.foundry;

import net.sparkmuse.task.Task;
import net.sparkmuse.data.FoundryDao;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.WishEmailEntry;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.discussion.FoundryFacade;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;

/**
 * @author neteller
 * @created: Mar 20, 2011
 */
public class HandleWishVoteTask extends Task {

  public static final String PARAM_WISH_ID = "PARAM_WISH_ID";
  public static final String PARAM_VOTER_USER_ID = "PARAM_VOTER_USER_ID";

  private FoundryFacade foundryFacade;
  private UserFacade userFacade;
  private FoundryDao foundryDao;

  @Inject
  public HandleWishVoteTask(ObjectDatastore datastore, FoundryFacade foundryFacade, UserFacade userFacade, FoundryDao foundryDao) {
    super(datastore);
    this.foundryFacade = foundryFacade;
    this.userFacade = userFacade;
    this.foundryDao = foundryDao;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    long wishId = Long.parseLong(getParameter(PARAM_WISH_ID));
    long voterUserId = Long.parseLong(getParameter(PARAM_VOTER_USER_ID));

    WishEmailEntry emailEntry = WishEmailEntry.newInstance(foundryFacade.findWishBy(wishId), userFacade.findUserProfileBy(voterUserId));
    if (null != emailEntry) foundryDao.store(emailEntry);

    return null;
  }


}
