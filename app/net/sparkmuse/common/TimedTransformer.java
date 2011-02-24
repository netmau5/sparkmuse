package net.sparkmuse.common;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Cursor;

import java.util.List;

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

  private Cursor lastSuccessfulTransform; //the cursor before the last transformation

  public TimedTransformer(Function<T, T> transformation) {
    this.transformation = transformation;
    this.millis = 5 * 60 * 1000; //5 minutes by default, task request limit is now 10 mins
  }

  public TimedTransformer(long millis, Function<T, T> transformation) {
    this.transformation = transformation;
    this.millis = millis;
  }

  public List<T> transform(QueryResultIterator<T> iterator) {
    final List<T> toReturn = Lists.newArrayList();
    long startTime = System.currentTimeMillis();
    while (iterator.hasNext()) {
      this.lastSuccessfulTransform = iterator.getCursor();
      toReturn.add(transformation.apply(iterator.next()));
      if (System.currentTimeMillis() - startTime >= millis) {
        break;
      }
    }
    return toReturn;
  }

  public Cursor cursorFromLastSuccessfulTransform() {
    return lastSuccessfulTransform;
  }

}
