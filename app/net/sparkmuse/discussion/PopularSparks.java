package net.sparkmuse.discussion;

import net.sparkmuse.data.Cacheable;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class PopularSparks extends AbstractSparkSearchResponse
    implements Cacheable<PopularSparks>, SparkSearchResponse {

  public PopularSparks(final List<SparkVO> sparks) {
    super(sparks, SparkSearchRequest.Filter.POPULAR);
  }

  public CacheKey<PopularSparks> getKey() {
    return CacheKeyFactory.newPopularSparksKey();
  }

  public PopularSparks getInstance() {
    return this;
  }
  
}
