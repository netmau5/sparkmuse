package net.sparkmuse.common;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.PopularSparks;

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
}
