package net.sparkmuse.data;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public interface ActivityDao extends CrudDao {

  List<Activity> findEveryone(int limit);

  List<Activity> findUser(UserVO user, DateTime after);

  List<Activity> findUser(UserVO user, Activity.Source source);

  void deleteAll(Activity.Kind kind, Long contentKey);
}
