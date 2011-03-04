package net.sparkmuse.data.twig;

import net.sparkmuse.data.ActivityDao;
import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserVO;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public class TwigActivityDao extends TwigDao implements ActivityDao {

  @Inject
  public TwigActivityDao(DatastoreService service) {
    super(service);
  }

  public List<Activity> findEveryone(int limit) {
    return helper.all(datastore.find().type(Activity.class)
        .addFilter("population", Query.FilterOperator.EQUAL, Activity.Population.EVERYONE.toString())
        .addSort("created", Query.SortDirection.DESCENDING)
        .fetchMaximum(limit));
  }

  public List<Activity> findUser(UserVO user, DateTime after) {
    return helper.all(datastore.find().type(Activity.class)
        .addFilter("userId", Query.FilterOperator.EQUAL, user.getId())
        .addSort("created", Query.SortDirection.DESCENDING)
        .addFilter("created", Query.FilterOperator.GREATER_THAN_OR_EQUAL, after.getMillis()));
  }

  public List<Activity> findUser(UserVO user, Activity.Source source) {
    return helper.all(datastore.find().type(Activity.class)
        .addFilter("userId", Query.FilterOperator.EQUAL, user.getId())
        .addSort("created", Query.SortDirection.DESCENDING)
        .addFilter("sources", Query.FilterOperator.EQUAL, source.toString())
        .fetchMaximum(20));
  }

}
