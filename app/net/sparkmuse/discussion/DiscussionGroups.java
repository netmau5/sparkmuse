package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.DiscussionGroup;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.common.Orderings;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import play.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Apr 9, 2011
 */
public class DiscussionGroups implements Serializable, Cacheable<DiscussionGroups> {

  private final List<DiscussionGroup> groups;

  public DiscussionGroups(List<DiscussionGroup> groups) {
    this.groups = new AlphabeticalOrdering().sortedCopy(groups);
  }

  public DiscussionGroup findGroupNamed(final String groupName) {
    if (StringUtils.isBlank(groupName) || StringUtils.equals(groupName, DiscussionGroup.GENERAL_DISCUSSION.getName())) {
      return DiscussionGroup.GENERAL_DISCUSSION;
    }
    
    try {
      return Iterables.find(this.groups, new Predicate<DiscussionGroup>(){
        public boolean apply(DiscussionGroup discussionGroup) {
          return discussionGroup.getName().equals(groupName);
        }
      });
    }
    catch (NoSuchElementException e) {
      Logger.error("User attempted to access a group [" + groupName + "] that does not exist.");
      return null;
    }
  }

  public DiscussionGroup findGroupBy(final Long groupId) {
    if (null == groupId) {
      return DiscussionGroup.GENERAL_DISCUSSION;
    }

    try {
      return Iterables.find(this.groups, new Predicate<DiscussionGroup>(){
        public boolean apply(DiscussionGroup discussionGroup) {
          return discussionGroup.getId().equals(groupId);
        }
      });
    }
    catch (NoSuchElementException e) {
      Logger.error("User attempted to access a group [" + groupId + "] that does not exist.");
      return null;
    }
  }

  public CacheKey<DiscussionGroups> getKey() {
    return CacheKeyFactory.newDiscussionGroupsKey();
  }

  public DiscussionGroups getInstance() {
    return this;
  }

  private static class AlphabeticalOrdering extends Orderings.SerializableOrdering<DiscussionGroup> {
    public int compare(DiscussionGroup discussionGroup, DiscussionGroup discussionGroup1) {
      return discussionGroup.getName().compareTo(discussionGroup1.getName());
    }
  }
  
}
