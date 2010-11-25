package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public class MostDiscussedSparks extends AbstractSparkSearchResponse
    implements Cacheable<MostDiscussedSparks>, SparkSearchResponse {


  public MostDiscussedSparks(final List<SparkVO> sparks) {
    super(sparks, SparkSearchRequest.Filter.DISCUSSED);
  }

  public CacheKey<MostDiscussedSparks> getKey() {
    return CacheKeyFactory.newMostDiscussedSparksKey();
  }

  public MostDiscussedSparks getInstance() {
    return this;
  }
}
