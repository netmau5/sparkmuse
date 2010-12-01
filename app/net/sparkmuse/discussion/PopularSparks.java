package net.sparkmuse.discussion;

import net.sparkmuse.data.Cacheable;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.common.NullTo;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class PopularSparks extends AbstractSparkSearchResponse
    implements Cacheable<PopularSparks>, SparkSearchResponse {

  public PopularSparks(final List<SparkVO> sparks) {
    super(newTreeSet(sparks), SparkSearchRequest.Filter.POPULAR);
  }

  public CacheKey<PopularSparks> getKey() {
    return CacheKeyFactory.newPopularSparksKey();
  }

  public PopularSparks getInstance() {
    return this;
  }

  private static TreeSet<SparkVO> newTreeSet(List<SparkVO> sparks) {
    final TreeSet<SparkVO> treeSet = new TreeSet<SparkVO>(new Comparator());
    treeSet.addAll(NullTo.empty(sparks));
    return treeSet;
  }

  static class Comparator implements java.util.Comparator<SparkVO> {
    public int compare(SparkVO a, SparkVO b) {
      return ((int) b.getRating() * 100) - ((int) a.getRating() * 100);
    }
  }
  
}
