package net.sparkmuse.data;

import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.data.entity.DiscussionContent;
import net.sparkmuse.data.entity.DiscussionComment;
import net.sparkmuse.data.entity.DiscussionGroup;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public interface DiscussionDao extends CrudDao {

  /**
   * @param groupId Nullable for general discussion
   * @return
   */
  List<Discussion> findMostRecentDiscussions(Long groupId);

  DiscussionContent findDiscussionContentBy(Long discussionId);

  Discussion findDiscussionBy(Long discussionId);

  List<DiscussionComment> findCommentsFor(Long discussionId);

  List<DiscussionGroup> findDiscussionGroups();
}
