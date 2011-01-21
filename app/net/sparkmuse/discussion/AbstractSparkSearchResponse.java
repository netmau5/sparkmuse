package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;

import java.util.TreeSet;
import java.io.Serializable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import play.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public abstract class AbstractSparkSearchResponse implements Serializable {

  static final int MAX_SIZE = 60;

  private final TreeSet<SparkVO> sparks;
  private SparkSearchRequest.Filter filter;

  public AbstractSparkSearchResponse(final TreeSet<SparkVO> sparks, final SparkSearchRequest.Filter filter) {
    Preconditions.checkNotNull(sparks);
    this.sparks = sparks;
    this.filter = filter;
  }

  public TreeSet<SparkVO> getSparks() {
    return sparks;
  }

  public SparkSearchRequest.Filter getFilter() {
    return filter;
  }

  public void update(SparkVO spark) {
    Logger.debug("Removing spark from SparkSearchResponse [" + spark.getKey() + "] with size [" + sparks.size() + "]");
    this.sparks.remove(spark); //different instances may .equal one another, remove old one
    Logger.debug("Adding spark from SparkSearchResponse [" + spark.getKey() + "] with size [" + sparks.size() + "]");
    this.sparks.add(spark);
    if (this.sparks.size() > MAX_SIZE) this.sparks.remove(Iterables.getLast(this.sparks));
  }
  
}
