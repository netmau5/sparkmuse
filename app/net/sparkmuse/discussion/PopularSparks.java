package net.sparkmuse.discussion;

import net.sparkmuse.data.Cacheable;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;

import java.util.List;

import controllers.Spark;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class PopularSparks implements Cacheable<PopularSparks> {

  private final List<SparkVO> sparks;

  /**
   * @param sparks top 50 sparks as ranked by their rating field
   */
  public PopularSparks(final List<SparkVO> sparks) {
    this.sparks = sparks;
  }

  public CacheKey<PopularSparks> getKey() {
    return CacheKeyFactory.newPopularSparksKey();
  }

  public PopularSparks getInstance() {
    return this;
  }

  public List<SparkVO> getSparks() {
    return sparks;
  }
  
}
