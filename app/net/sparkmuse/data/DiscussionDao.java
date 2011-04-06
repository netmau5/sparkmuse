package net.sparkmuse.data;

import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.data.entity.DiscussionContent;
import net.sparkmuse.data.entity.DiscussionComment;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public interface DiscussionDao extends CrudDao {

  List<Discussion> findMostRecentDiscussions();

  DiscussionContent findDiscussionContentBy(Long discussionId);

  Discussion findDiscussionBy(Long discussionId);

  List<DiscussionComment> findCommentsFor(Long discussionId);
}
