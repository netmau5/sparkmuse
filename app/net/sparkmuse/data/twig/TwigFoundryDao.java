package net.sparkmuse.data.twig;

import net.sparkmuse.data.FoundryDao;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.entity.Comment;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;
import com.google.code.twig.FindCommand;

import java.util.List;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
public class TwigFoundryDao extends TwigDao implements FoundryDao {

  @Inject
  public TwigFoundryDao(DatastoreService service) {
    super(service);
  }

  public List<Wish> findRecentWishes(PageChangeRequest pageChangeRequest) {
    FindCommand.RootFindCommand<Wish> findCommand = datastore.find().type(Wish.class)
        .addSort("created", Query.SortDirection.DESCENDING);
    return helper.all(pageChangeRequest, findCommand);
  }

  public List<Wish> findTaggedWishes(String tag, PageChangeRequest pageChangeRequest) {
    FindCommand.RootFindCommand<Wish> findCommand = datastore.find().type(Wish.class)
        .addFilter("tags", Query.FilterOperator.EQUAL, tag)
        .addSort("created", Query.SortDirection.DESCENDING);
    return helper.all(pageChangeRequest, findCommand);
  }

  public List<Comment> findWishCommentsBy(Long wishId) {
    FindCommand.RootFindCommand<Comment> findCommand = datastore.find().type(Comment.class)
        .addFilter("wishId", Query.FilterOperator.EQUAL, wishId)
        .addSort("created", Query.SortDirection.DESCENDING);
    return helper.all(findCommand);
  }
}
