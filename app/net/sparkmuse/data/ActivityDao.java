package net.sparkmuse.data;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public interface ActivityDao extends CrudDao {

  /**
   *
   * @param after nullable
   * @param limit
   * @return
   */
  List<Activity> findEveryone(DateTime after, int limit);

  List<Activity> findUser(UserVO user, DateTime after, int limit);

  List<Activity> findUser(UserVO user, Set<Activity.Source> source, int limit);

  void deleteAll(Activity.Kind kind, Long contentKey);
}
