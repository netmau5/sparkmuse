package net.sparkmuse.data.twig;

import net.sparkmuse.data.FoundryDao;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.data.entity.*;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.code.twig.FindCommand;
import com.google.code.twig.standard.KeySpecification;
import com.google.code.twig.standard.TranslatorObjectDatastore;

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

  public List<Wish> findPopularWishes(PageChangeRequest pageChangeRequest) {
    FindCommand.RootFindCommand<Wish> findCommand = datastore.find().type(Wish.class)
        .addSort("votes", Query.SortDirection.DESCENDING);
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
    return AbstractComment.applyHierarchy(helper.all(findCommand));
  }

  public List<Commitment> findCommitmentsFor(Long requestingUserId, Long wishId) {
    FindCommand.RootFindCommand<Commitment> findCommand = datastore.find().type(Commitment.class)
        .addFilter("userId", Query.FilterOperator.EQUAL, requestingUserId)
        .addFilter("wishId", Query.FilterOperator.EQUAL, wishId);
    return helper.all(findCommand);
  }

  public List<Wish> findTopWishes() {
    FindCommand.RootFindCommand<Wish> findCommand = datastore.find().type(Wish.class)
        .addSort("votes", Query.SortDirection.DESCENDING)
        .fetchMaximum(5)
        .fetchFirst(5);
    return helper.all(findCommand);
  }
}
