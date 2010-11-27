package net.sparkmuse.discussion;

import net.sparkmuse.data.PostDao;
import net.sparkmuse.data.SparkDao;
import net.sparkmuse.data.DaoProvider;
import net.sparkmuse.data.WriteThruCacheService;
import net.sparkmuse.data.util.Posts;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.PostVO;
import net.sparkmuse.discussion.VoteSorter;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.user.UserFacade;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.common.base.Preconditions;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class SparkFacade {

  private final SparkDao sparkDao;
  private final PostDao postDao;
  private final WriteThruCacheService cache;
  private final UserFacade userFacade;

  @Inject
  public SparkFacade(DaoProvider daoProvider,
                     WriteThruCacheService cache,
                     UserFacade userFacade
  ) {
    this.sparkDao = daoProvider.getSparkDao();
    this.postDao = daoProvider.getPostDao();
    this.cache = cache;
    this.userFacade = userFacade;
  }

  public SparkSearchResponse search(final SparkSearchRequest.Filter filter) {
    if (SparkSearchRequest.Filter.DISCUSSED == filter) return getMostDiscussedSparks();
    else if (SparkSearchRequest.Filter.POPULAR == filter) return getPopularSparks();
    else if (SparkSearchRequest.Filter.RECENT == filter) return getRecentSparks();
    else throw new IllegalArgumentException("Unknown spark search request.");
  }

  private PopularSparks getPopularSparks() {
    PopularSparks sparks = cache.get(CacheKeyFactory.newPopularSparksKey());
    if (null == sparks) {
      sparks = cache.put(new PopularSparks(sparkDao.loadPopular()));
    }
    return sparks;
  }

  private MostDiscussedSparks getMostDiscussedSparks() {
    MostDiscussedSparks sparks = cache.get(CacheKeyFactory.newMostDiscussedSparksKey());
    if (null == sparks) {
      sparks = cache.put(new MostDiscussedSparks(sparkDao.loadMostDiscussed()));
    }
    return sparks;
  }

  private RecentSparks getRecentSparks() {
    RecentSparks sparks = cache.get(CacheKeyFactory.newRecentSparksKey());
    if (null == sparks) {
      sparks = cache.put(new RecentSparks(sparkDao.loadRecent()));
    }
    return sparks;
  }

  public Posts findPostsFor(final SparkVO spark) {
    Collection<PostVO> posts = postDao.findPostsBySpark(spark);
    return new Posts(spark, VoteSorter.sortPosts(posts));
  }

  public SparkVO findSparkBy(final Long id) {
    final SparkVO sparkVO = cache.get(CacheKeyFactory.newSparkKey(id));
    return null == sparkVO ? sparkDao.findById(id) : sparkVO;
  }

  public SparkVO createSpark(final SparkVO spark) {
    Preconditions.checkArgument(null == spark.getId());
    final SparkVO newSparkVO = sparkDao.create(spark);
    cache.put(newSparkVO);

    //author implicitly votes for spark; thus, they will not be able to vote for it again
    userFacade.recordUpVote(newSparkVO, newSparkVO.getAuthor().getId());

    final RecentSparks recentSparks = getRecentSparks();
    recentSparks.addNew(newSparkVO);
    cache.put(recentSparks);

    return newSparkVO;
  }

}
