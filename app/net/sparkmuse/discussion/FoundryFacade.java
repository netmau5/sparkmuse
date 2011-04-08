package net.sparkmuse.discussion;

import net.sparkmuse.common.Cache;
import net.sparkmuse.common.CommitmentType;
import net.sparkmuse.common.Orderings;
import net.sparkmuse.data.FoundryDao;
import net.sparkmuse.data.DaoProvider;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.Votable;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.IncrementUserStatisticTask;
import com.google.inject.Inject;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
  private final IssueTaskService issueTaskService;
  private final Cache cache;

  @Inject
  public FoundryFacade(DaoProvider daoProvider, UserFacade userFacade, IssueTaskService issueTaskService, Cache cache) {
    this.foundryDao = daoProvider.getFoundryDao();
    this.cache = cache;
    this.issueTaskService = issueTaskService;
    this.userFacade = userFacade;
  }

  public List<String> getTopTags() {
    TagCount count = cache.get(TagCount.CACKEKEY_WISH_TAG_COUNTER, TagCount.class);
    if (null == count) {
      count = foundryDao.load(TagCount.class, TagCount.NAME_WISH_TAG_COUNTER);
      if (null != count) cache.set(TagCount.CACKEKEY_WISH_TAG_COUNTER, count);
    }
    return null == count ? Lists.<String>newArrayList() : count.getTopTags();
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
        getTopTags(),
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
        getTopTags(),
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
    if (isNew) {
      wish.getAuthor().issueIncrementTask(issueTaskService, UserVO.Statistic.WISH);
    }

    return wish;
  }

  public Wish findWishBy(Long id) {
    return foundryDao.load(Wish.class, id);
  }

  public WishResponse findWishContent(Long wishId, UserVO requestingUser) {
    Wish wish = findWishBy(wishId);
    List<Comment> comments = findWishCommentsBy(wishId);

    Set<Votable> votables = Sets.<Votable>newHashSet(comments);
    votables.add(wish);
    
    return new WishResponse(
        wish,
        comments,
        userFacade.findUserVotesFor(votables, requestingUser),
        findCommitmentsFor(requestingUser.getId(), wishId)
    );
  }

  public void commit(Wish wish, CommitmentType type, UserVO requestingUser) {
    userFacade.recordUpVote(wish, requestingUser.getId());
    Wish.commit(issueTaskService, requestingUser.getId(), wish.getId(), type);
  }

  public List<Commitment> findCommitmentsFor(Long userId, Long wishId) {
    return foundryDao.findCommitmentsFor(userId, wishId);
  }

  private List<Comment> findWishCommentsBy(Long wishId) {
    return Orderings.sort(foundryDao.findWishCommentsBy(wishId));
  }

  public Comment createComment(Comment comment) {
    Preconditions.checkArgument(null == comment.getId());
    final Comment newComment = foundryDao.store(comment);

    //modify post count
    final Wish wish = findWishBy(comment.getWishId());
    wish.setCommentCount(wish.getCommentCount() + 1);
    foundryDao.store(wish);
    newComment.getAuthor().issueIncrementTask(issueTaskService, UserVO.Statistic.POST);

    //author implicitly votes for post; thus, they will not be able to vote for it again
    userFacade.recordUpVote(newComment, newComment.getAuthor().getId());

    return newComment;
  }

  public TopWishes getTopWishes() {
    TopWishes topWishes = cache.get(TopWishes.CACHE_KEY);
    if (null == topWishes || topWishes.hasExpired() || topWishes.isEmpty()) {
      topWishes = new TopWishes(foundryDao.findTopWishes());
      cache.set(topWishes);
    }
    return topWishes;
  }

}
