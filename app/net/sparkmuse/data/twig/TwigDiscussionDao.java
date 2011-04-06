package net.sparkmuse.data.twig;

import net.sparkmuse.data.DiscussionDao;
import net.sparkmuse.data.entity.*;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import com.google.code.twig.FindCommand;

import java.util.List;
import java.util.Collection;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class TwigDiscussionDao extends TwigDao implements DiscussionDao {

  @Inject
  public TwigDiscussionDao(DatastoreService service) {
    super(service);
  }

  public List<Discussion> findMostRecentDiscussions() {
    FindCommand.RootFindCommand<Discussion> find = datastore.find().type(Discussion.class)
        .addSort("created", Query.SortDirection.DESCENDING)
        .fetchMaximum(100)
        .fetchFirst(100);
    return helper.all(find);
  }

  public DiscussionContent findDiscussionContentBy(Long discussionId) {
    final Discussion discussion = findDiscussionBy(discussionId);

    if (null == discussion) return null;

    return helper.only(datastore.find()
        .type(DiscussionContent.class)
        .ancestor(discussion)
    );
  }

  public Discussion findDiscussionBy(Long discussionId) {
    return helper.load(Discussion.class, discussionId);
  }

  public List<DiscussionComment> findCommentsFor(Long discussionId) {
    return AbstractComment.applyHierarchy(helper.all(
        datastore.find().type(DiscussionComment.class).addFilter("discussionId", EQUAL, discussionId)
    ));
  }
}
