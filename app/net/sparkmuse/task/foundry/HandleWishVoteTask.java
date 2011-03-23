package net.sparkmuse.task.foundry;

import net.sparkmuse.task.Task;
import net.sparkmuse.data.FoundryDao;
import net.sparkmuse.data.entity.Commitment;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.discussion.FoundryFacade;
import net.sparkmuse.common.CommitmentType;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;

import java.util.List;

/**
 * @author neteller
 * @created: Mar 20, 2011
 */
public class HandleWishVoteTask extends Task {

  public static final String PARAM_WISH_ID = "PARAM_WISH_ID";
  public static final String PARAM_VOTER_USER_ID = "PARAM_VOTER_USER_ID";
  public static final String PARAM_COMMITMENT_TYPE = "PARAM_COMMITMENT_TYPE";

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
    final CommitmentType commitmentType = CommitmentType.valueOf(getParameter(PARAM_COMMITMENT_TYPE));

    List<Commitment> commitments = foundryFacade.findCommitmentsFor(voterUserId, wishId);
    boolean hasCommitment = Iterables.any(commitments, new Predicate<Commitment>(){
      public boolean apply(Commitment commitment) {
        return commitmentType == commitment.getCommitmentType();
      }
    });


    if (!hasCommitment) {
      Wish wish = foundryFacade.findWishBy(wishId);
      Commitment commitment = Commitment.newInstance(
          wish,
          userFacade.findUserProfileBy(voterUserId),
          commitmentType
      );
      foundryDao.store(commitment);
      
      commitmentType.getCounter().increment(wish);
    }

    return null;
  }


}
