package net.sparkmuse.common;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ArrayList;

/**
 * @author neteller
 * @created: Jan 8, 2011
 */
public class Entries {

  public static <T, U> List<U> values(List<Entry<T, U>> entries) {
    final ArrayList<U> toReturn = Lists.newArrayList();
    for (Entry<T, U> entry: entries) {
      toReturn.add(entry.getValue());
    }
    return toReturn;
  }

}
