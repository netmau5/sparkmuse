package net.sparkmuse.discussion;

import net.sparkmuse.common.Cache;
import net.sparkmuse.data.FoundryDao;
import net.sparkmuse.data.DaoProvider;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.Votable;
import com.google.inject.Inject;
import com.google.common.collect.Sets;

import java.util.List;

import jj.play.org.eclipse.mylyn.wikitext.core.util.anttask.MarkupToXslfoTask;
import org.joda.time.DateTime;

/**
 * Manages wishes and comments within The Spark Foundry.
 *
 * @author neteller
 * @created: Mar 15, 2011
 */
public class FoundryFacade {

  private final FoundryDao foundryDao;
  private final UserFacade userFacade;
  private final Cache cache;

  @Inject
  public FoundryFacade(DaoProvider daoProvider, UserFacade userFacade, Cache cache) {
    this.foundryDao = daoProvider.getFoundryDao();
    this.cache = cache;
    this.userFacade = userFacade;
  }

  /**
   * Find recent wishes.
   *
   * @param user      user performing the search, can be null
   * @param request
   * @return
   */
  public WishSearchResponse findRecentWishes(UserVO user, PageChangeRequest request) {
    List<Wish> wishes = foundryDao.findRecentWishes(request);
    return new WishSearchResponse(
        wishes,
        userFacade.findUserVotesFor(Sets.<Votable>newHashSet(wishes), user),
        request.getState()
    );
  }

  /**
   * Find wishes by tag.
   *
   * @param user      user performing the search, can be null
   * @param request
   * @return
   */
  public WishSearchResponse findTaggedWishes(String tag, UserVO user, PageChangeRequest request) {
    List<Wish> wishes = foundryDao.findTaggedWishes(tag, request);
    return new WishSearchResponse(
        wishes,
        userFacade.findUserVotesFor(Sets.<Votable>newHashSet(wishes), user),
        request.getState()
    );
  }

  public Wish store(Wish wish) {
    boolean isNew = null == wish.getId();

    if (!isNew) {
      wish.setEdited(new DateTime());
    }

    //make sure tags are lowercase for easier querying
    wish = wish.updateTitleTokens().lowercaseTags();

    foundryDao.store(wish);

    //author implicitly votes for spark; thus, they will not be able to vote for it again
    userFacade.recordUpVote(wish, wish.getAuthor().getId());

    return wish;
  }

  public Wish findWishBy(Long id) {
    return foundryDao.load(Wish.class, id);
  }

}
