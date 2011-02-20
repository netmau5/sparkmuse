package net.sparkmuse.task;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.code.twig.Restriction;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterator;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.util.AccessLevel;
import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Feb 20, 2011
 */
public class GatherAppStatisticsTask extends Task {

  private final ObjectDatastore datastore;

  @Inject
  public GatherAppStatisticsTask(ObjectDatastore datastore) {
    super(datastore);
    this.datastore = datastore;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    //no filter used for isUSER so we dont need an index
    Future<Integer> userCount = createCountQuery(UserVO.class).restrictEntities(new ActiveUserRestriction()).returnCount().later();
    Future<Integer> sparkCount = createCountQuery(SparkVO.class).returnCount().later();
    Future<Integer> postCount = createCountQuery(Post.class).returnCount().later();

    //get previous counts
    Future<QueryResultIterator<AppStat>> previousUserCount = createPreviousCountQuery(AppStat.Type.USERS);
    Future<QueryResultIterator<AppStat>> previousSparkCount = createPreviousCountQuery(AppStat.Type.USERS);
    Future<QueryResultIterator<AppStat>> previousPostCount = createPreviousCountQuery(AppStat.Type.USERS);

    try {
      datastore.store().instance(AppStat.newUserCount(previousCount(previousUserCount) + userCount.get())).later();
      datastore.store().instance(AppStat.newSparkCount(previousCount(previousSparkCount) + sparkCount.get())).later();
      datastore.store().instance(AppStat.newPostCount(previousCount(previousPostCount) + postCount.get())).later();
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  private int previousCount(Future<QueryResultIterator<AppStat>> query) throws ExecutionException, InterruptedException {
    QueryResultIterator<AppStat> statQueryResultIterator = query.get();
    if (statQueryResultIterator.hasNext()) {
      return statQueryResultIterator.next().getCount();
    }
    else {
      return 0;
    }
  }

  private Future<QueryResultIterator<AppStat>> createPreviousCountQuery(AppStat.Type type) {
    return datastore.find().type(AppStat.class)
        .addFilter("type", Query.FilterOperator.EQUAL, type.toString())
        .addSort("created", Query.SortDirection.DESCENDING)
        .fetchMaximum(1)
        .later();
  }

  private <T> FindCommand.RootFindCommand<T> createCountQuery(Class<T> type) {
    return sinceLastMigration(datastore.find().type(type));
  }


  private <T> FindCommand.RootFindCommand<T> sinceLastMigration(FindCommand.RootFindCommand<T> find) {
    final Migration lastMigration = lastMigration();
    if (null != lastMigration) { //has a value set for people elgible
      find.addFilter("created", Query.FilterOperator.GREATER_THAN, lastMigration.getStarted().getMillis());
    }
    return find;
  }

  private static final class ActiveUserRestriction implements Restriction<com.google.appengine.api.datastore.Entity> {
    public boolean allow(com.google.appengine.api.datastore.Entity candidate) {
      return StringUtils.equalsIgnoreCase(candidate.getProperty("accessLevel").toString(), AccessLevel.USER.toString());
    }
  }

}
