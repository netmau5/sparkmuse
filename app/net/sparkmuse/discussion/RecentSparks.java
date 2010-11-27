package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;

import java.util.List;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public class RecentSparks extends AbstractSparkSearchResponse
    implements Cacheable<RecentSparks>, SparkSearchResponse {

  public RecentSparks(final List<SparkVO> sparks) {
    super(sparks, SparkSearchRequest.Filter.RECENT);
  }

  public CacheKey<RecentSparks> getKey() {
    return CacheKeyFactory.newRecentSparksKey();
  }

  public RecentSparks getInstance() {
    return this;
  }

  public RecentSparks addNew(final SparkVO newSpark) {
    if (CollectionUtils.size(this.getSparks()) > MAX_SIZE) {
      this.getSparks().remove(this.getSparks().size() - 1);
    }
    this.getSparks().add(0, newSpark);
    return this;
  }
}
