package net.sparkmuse.data;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserVO;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public interface ActivityDao extends CrudDao {

  List<Activity> findEveryone(int limit);

  List<Activity> findUser(UserVO user, DateTime after);

}
