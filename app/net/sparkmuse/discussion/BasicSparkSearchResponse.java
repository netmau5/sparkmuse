package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.paging.PagingState;

import java.util.TreeSet;
import java.io.Serializable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import play.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public class BasicSparkSearchResponse implements Serializable, SparkSearchResponse {

  private final TreeSet<SparkVO> sparks;
  private SparkSearchRequest.Filter filter;

  public BasicSparkSearchResponse(final TreeSet<SparkVO> sparks, final SparkSearchRequest.Filter filter) {
    Preconditions.checkNotNull(sparks);
    this.sparks = sparks;
    this.filter = filter;
  }

  public TreeSet<SparkVO> getSparks() {
    return sparks;
  }

  public TreeSet<SparkVO> getSparks(PagingState state) {
    return getSparks();
  }

  public SparkSearchRequest.Filter getFilter() {
    return filter;
  }

  public void update(SparkVO spark) {
    Logger.debug("Removing spark from SparkSearchResponse [" + spark.getKey() + "] with size [" + sparks.size() + "]");
    this.sparks.remove(spark); //different instances may .equal one another, remove old one
    Logger.debug("Adding spark from SparkSearchResponse [" + spark.getKey() + "] with size [" + sparks.size() + "]");
    this.sparks.add(spark);
    if (this.sparks.size() > MAX_CACHE_SIZE) this.sparks.remove(Iterables.getLast(this.sparks));
  }
  
}
