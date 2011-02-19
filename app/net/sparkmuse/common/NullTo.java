package net.sparkmuse.common;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Collection;

import org.joda.time.DateTime;
import net.sparkmuse.data.paging.PagingState;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 30, 2010
 */
public class NullTo {

  public static <T> List<T> empty(List<T> list) {
    if (null == list) return Lists.newArrayList();
    else return list;
  }

  public static <T> Collection<T> empty(Collection<T> list) {
    if (null == list) return Lists.newArrayList();
    else return list;
  }

  public static String empty(String s) {
    if (null == s) return "";
    return s;
  }

  public static DateTime now(DateTime time) {
    if (null == time) return new DateTime();
    return time;
  }

}
