package net.sparkmuse.common;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.PopularSparks;
import net.sparkmuse.discussion.RecentSparks;
import net.sparkmuse.discussion.MostDiscussedSparks;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class CacheKeyFactory {
  public static CacheKey<UserVO> newUserKey(final Long id) {
    return new CacheKey(UserVO.class, id);
  }
  public static CacheKey<SparkVO> newSparkKey(final Long id) {
    return new CacheKey(SparkVO.class, id);
  }
  public static CacheKey<PopularSparks> newPopularSparksKey() {
    return new CacheKey(PopularSparks.class);
  }
  public static CacheKey<RecentSparks> newRecentSparksKey() {
    return new CacheKey(RecentSparks.class);
  }
  public static CacheKey<MostDiscussedSparks> newMostDiscussedSparksKey() {
    return new CacheKey(MostDiscussedSparks.class);
  }
}
