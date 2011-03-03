package net.sparkmuse.discussion;

import net.sparkmuse.data.*;
import net.sparkmuse.data.paging.PagingState;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.Orderings;
import net.sparkmuse.user.UserFacade;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import com.google.inject.Inject;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.apache.commons.lang.StringUtils;
import play.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class SparkFacade {

  private final SparkDao sparkDao;
  private final PostDao postDao;
  private final Cache cache;
  private final UserFacade userFacade;

  @Inject
  public SparkFacade(DaoProvider daoProvider,
                     Cache cache,
                     UserFacade userFacade
  ) {
    this.sparkDao = daoProvider.getSparkDao();
    this.postDao = daoProvider.getPostDao();
    this.cache = cache;
    this.userFacade = userFacade;
  }

  public SparkSearchResponse search(final SparkSearchRequest request) {
    PageChangeRequest pageChange = request.getPageChangeRequest();
    if (request.getFilter() == SparkSearchRequest.Filter.TAGGED) {
      return getTaggedSparks(request.getTag());
    }
    //cacheable + pageable stuff
    else if(SparkSearchResponse.MAX_CACHE_SIZE <= pageChange.maxResultIndex()) {
      TreeSet results = new TreeSet(request.getFilter().getOrdering());
      results.addAll(sparkDao.search(request));
      return new BasicSparkSearchResponse(results, request.getFilter());
    }
    //pageable
    else {
      //paging state wont be updated by persistence as we are using cached stuff
      SparkSearchResponse response = getCachedSparkSearchResponse(request);
      pageChange.transition(response.getSparks().size() > pageChange.maxResultIndex(), null);
      return response;
    } 
  }

  private SparkSearchResponse getCachedSparkSearchResponse(SparkSearchRequest request) {
    SparkSearchRequest.Filter filter = request.getFilter();
    if (SparkSearchRequest.Filter.DISCUSSED == filter) return getMostDiscussedSparks();
    else if (SparkSearchRequest.Filter.POPULAR == filter) return getPopularSparks();
    else if (SparkSearchRequest.Filter.RECENT == filter) return getRecentSparks();
    else throw new IllegalArgumentException("Unknown spark search request.");
  }

  private SparkSearchResponse getTaggedSparks(final String tag) {
    TreeSet taggedSparks = new TreeSet(new Orderings.ByRecency());
    taggedSparks.addAll(sparkDao.loadTagged(tag));
    return new BasicSparkSearchResponse(taggedSparks, SparkSearchRequest.Filter.TAGGED);
  }

  private PopularSparks getPopularSparks() {
    PopularSparks sparks = cache.get(CacheKeyFactory.newPopularSparksKey());
    if (null == sparks) {
      Logger.info("Reloading popular sparks.");
      sparks = cache.set(new PopularSparks(sparkDao.loadPopular()));
    }
    return sparks;
  }

  private MostDiscussedSparks getMostDiscussedSparks() {
    MostDiscussedSparks sparks = cache.get(CacheKeyFactory.newMostDiscussedSparksKey());
    if (null == sparks) {
      Logger.info("Reloading most discussed sparks.");
      sparks = cache.set(new MostDiscussedSparks(sparkDao.loadMostDiscussed()));
    }
    return sparks;
  }

  private RecentSparks getRecentSparks() {
    RecentSparks sparks = cache.get(CacheKeyFactory.newRecentSparksKey());
    if (null == sparks) {
      Logger.info("Reloading recent sparks.");
      sparks = cache.set(new RecentSparks(sparkDao.loadRecent()));
    }
    return sparks;
  }

  public Posts findPostsFor(final SparkVO spark) {
    Collection<Post> posts = postDao.findPostsBySpark(spark);
    return new Posts(spark, Orderings.sort(posts));
  }

  public Post findPostBy(final Long id) {
    return postDao.load(Post.class, id);
  }

  public SparkVO findSparkBy(final Long id) {
    final SparkVO sparkVO = cache.get(CacheKeyFactory.newSparkKey(id));
    return null == sparkVO ? sparkDao.findById(id) : sparkVO;
  }

  public SparkVO storeSpark(final SparkVO spark) {
    boolean isNew = null == spark.getId();

    if (!isNew) {
      spark.setEdited(new DateTime());
    }

    //make sure tags are lowercase for easier querying
    spark.lowercaseTags();

    final SparkVO newSparkVO = sparkDao.store(spark);

    //author implicitly votes for spark; thus, they will not be able to vote for it again
    userFacade.recordUpVote(newSparkVO, newSparkVO.getAuthor().getId());

    if (isNew) {
      userFacade.recordNewSpark(newSparkVO.getAuthor());
    }

    return newSparkVO;
  }

  public Post createPost(final Post post) {
    Preconditions.checkArgument(null == post.getId());
    final Post newPost = postDao.store(post);

    //modify post count
    final SparkVO spark = findSparkBy(post.getSparkId());
    spark.setPostCount(spark.getPostCount() + 1);
    sparkDao.store(spark);
    userFacade.recordNewPost(newPost.getAuthor());

    //author implicitly votes for post; thus, they will not be able to vote for it again
    userFacade.recordUpVote(newPost, newPost.getAuthor().getId());

    return newPost;
  }

}
