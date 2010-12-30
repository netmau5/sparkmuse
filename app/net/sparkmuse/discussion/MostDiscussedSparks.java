package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.common.NullTo;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public class MostDiscussedSparks extends AbstractSparkSearchResponse
    implements Cacheable<MostDiscussedSparks>, SparkSearchResponse {


  public MostDiscussedSparks(final List<SparkVO> sparks) {
    super(newTreeSet(sparks), SparkSearchRequest.Filter.DISCUSSED);
  }

  public CacheKey<MostDiscussedSparks> getKey() {
    return CacheKeyFactory.newMostDiscussedSparksKey();
  }

  public MostDiscussedSparks getInstance() {
    return this;
  }

  private static TreeSet<SparkVO> newTreeSet(List<SparkVO> sparks) {
    final TreeSet<SparkVO> treeSet = new TreeSet<SparkVO>(new Comparator());
    treeSet.addAll(NullTo.empty(sparks));
    return treeSet;
  }

  static class Comparator implements java.util.Comparator<SparkVO> {
    public int compare(SparkVO a, SparkVO b) {
      final int postCount = b.getPostCount() - a.getPostCount();
      return tiebreak(a, b, postCount);
    }
  }

}
