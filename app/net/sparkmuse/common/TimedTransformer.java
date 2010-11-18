package net.sparkmuse.common;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Iterator;

/**
 * Provides object transformation logic in a time-sensitive executor.
 * The timing provided is tested on each application of the transformation
 * and isn't exactly 15 seconds.
 *
 * @author neteller
 * @created: Nov 7, 2010
 */
public class TimedTransformer<T> {

  private final Function<T, T> transformation;
  private final long millis;

  public TimedTransformer(Function<T, T> transformation) {
    this.transformation = transformation;
    this.millis = 15 * 1000; //15 seconds by default
  }

  public TimedTransformer(long millis, Function<T, T> transformation) {
    this.transformation = transformation;
    this.millis = millis;
  }

  public List<T> transform(Iterator<T> iterator) {
    final List<T> toReturn = Lists.newArrayList();
    long startTime = System.currentTimeMillis();
    while (iterator.hasNext()) {
      toReturn.add(transformation.apply(iterator.next()));
      if (System.currentTimeMillis() - startTime >= millis) {
        break;
      }
    }
    return toReturn;
  }



}
