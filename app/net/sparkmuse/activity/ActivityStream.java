package net.sparkmuse.activity;

import net.sparkmuse.data.ActivityDao;
import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.UserVO;

import java.util.List;
import java.util.TreeSet;
import java.util.Set;
import java.io.Serializable;

import org.joda.time.DateTime;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.inject.internal.ImmutableList;

/**
 * @author neteller
 * @created: Feb 12, 2011
 */
public class ActivityStream implements Serializable {

  private final ImmutableList<Activity> activities;

  /**
   * @param activities  should be ordered by created descending
   */
  ActivityStream(List<Activity> activities) {
    this.activities = ImmutableList.copyOf(activities);
  }

  public static ActivityStream.Builder builder(ActivityDao activityDao) {
    return new ActivityStream.Builder(activityDao);
  }

  public List<Activity> getActivities() {
    return Lists.newArrayList(activities);
  }

  public DateTime getOldestTime() {
    if (this.activities.size() == 0) return new DateTime();
    return Iterables.getLast(this.activities).getCreated();
  }

  /**
   * Overlays the passed in activity stream on top of the existing one
   * and returns a new one.  Any duplicates (as defined by Activity.compareTo)
   * are taken from the parameterized stream.
   *
   * @param userActivity
   * @return
   */
  public ActivityStream overlay(ActivityStream userActivity) {
    TreeSet<Activity> set = Sets.newTreeSet(userActivity.activities);
    //apply this instance afterwards, duplicates will fail to be added to set
    set.addAll(activities);
    return new ActivityStream(Lists.newArrayList(set));
  }

  static class Builder {

    private final ActivityDao activityDao;

    private UserVO user;
    private DateTime after;
    private Set<Activity.Source> sources;
    private int fetch = 50;

    private Builder(ActivityDao activityDao) {
      this.activityDao = activityDao;
      this.sources = Sets.newHashSet();
    }

    public Builder forUser(UserVO user) {
      this.user = user;
      return this;
    }

    public Builder after(DateTime dateTime) {
      this.after = dateTime;
      return this;
    }

    public Builder in(Activity.Source source) {
      this.sources.add(source);
      return this;
    }

    public Builder fetch(int count) {
      this.fetch = count;
      return this;
    }

    public ActivityStream build() {
      Preconditions.checkState(!(null != after && CollectionUtils.size(sources) != 0), "Cannot query on both created and source.");
      if (null == user) {
        return new ActivityStream(activityDao.findEveryone(after, fetch));
      }
      else if (null != after) {
        Preconditions.checkNotNull(after);
        return new ActivityStream(mergeDuplicates(activityDao.findUser(user, after, fetch)));
      }
      else if (CollectionUtils.size(sources) != 0) {
        return new ActivityStream(mergeDuplicates(activityDao.findUser(user, sources, fetch)));
      }
      else {
        throw new IllegalStateException("Invalid query");
      }
    }

    private static List<Activity> mergeDuplicates(List<Activity> activities) {
      List<Activity> toReturn = Lists.newArrayList();
      for (Activity activity: activities) {
        int index = toReturn.indexOf(activity);
        if (index >= 0) {
          Activity existingActivity = toReturn.get(index);
          existingActivity.getSources().addAll(activity.getSources());
          Activity.ItemSummary existingSummary = existingActivity.getSummary();
          if (StringUtils.isBlank(existingSummary.getNote())) {
            existingSummary.setNote(activity.getSummary().getNote());
          }
          else if (StringUtils.isNotBlank(activity.getSummary().getNote())) {
            existingSummary.setNote(
                existingSummary.getNote() +
                ", " +
                activity.getSummary().getNote().substring(0, 1).toLowerCase() +
                activity.getSummary().getNote().substring(1)
            );
          }
        }
        else {
          toReturn.add(activity);
        }
      }
      return toReturn;
    }

  }

}
