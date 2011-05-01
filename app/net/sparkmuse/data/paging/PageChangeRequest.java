package net.sparkmuse.data.paging;

import net.sparkmuse.common.Cache;
import net.sparkmuse.data.entity.UserVO;
import com.google.code.twig.FindCommand;
import com.google.appengine.api.datastore.Cursor;

/**
 * @author neteller
 * @created: Feb 19, 2011
 */
public class PageChangeRequest {

  public static PageChangeRequest NO_PAGING = new PageChangeRequest(null, null, null);

  private final Target target;
  private final PagingState state;
  private final Cache cache;

  public static class Target {

    public Target(int currentPage, int newPage) {
      this.modifier = newPage - currentPage;
    }

    private final int modifier;

    public boolean isPrevious() {
      return modifier < 0;
    }

    public boolean isSame() {
      return 0 == modifier;
    }

    public boolean isNext() {
      return modifier > 0;
    }
  }

  PageChangeRequest(Target target, PagingState state, Cache cache) {
    this.target = target;
    this.state = state;
    this.cache = cache;
  }

  public PagingState getState() {
    return state;
  }

  public <T> FindCommand.RootFindCommand<T> applyPaging(FindCommand.RootFindCommand<T> find) {
    if (this == NO_PAGING) return find;

    int newPage = this.state.currentPage() + this.target.modifier;
    Cursor cursor = state.cursorFromPage(newPage - 1);
    int pageSize = this.state.pageSize();
    
    find.fetchMaximum(pageSize + 1);
    find.fetchFirst(pageSize + 1);

    //A cursor is not a relative position in the list (it's not an offset);
    //it's a marker to which the datastore can jump when starting an index scan for results
    if (null != cursor) {
      find.continueFrom(cursor);
    }

    find.startFrom(this.state.calculateOffset(newPage));

    return find;
  }

  public int maxResultIndex() {
    if (NO_PAGING == this) return 999;
    return this.state.pageSize() * (this.target.modifier + this.state.currentPage());
  }

  /**
   * After a page result has been calculated, set the existing page state from this point.
   * Do we have more results (show next button), what page are we on?
   * 
   * @param hasNext
   * @param cursor  Nullable
   */
  public void transition(boolean hasNext, Cursor cursor) {
    if (NO_PAGING == this) return;

    this.state.transition(this.target.modifier, hasNext, cursor);

    this.cache.set(this.state);
  }

  /**
   * Creates a new PageChangeRequest.  The existing page state is retrieved from the cache or initialized at
   * page 1.
   *
   * @param newPage       page requested
   * @param cache
   * @param sessionId
   * @param type          underlying object type that is being paged
   * @param uniqueId      nullable; if there is more than one paging mechanism applied to this object type,
   *                      this is distinguishes (ie, multiple types of entity results to be paged through)
   * @return
   */
  public static PageChangeRequest newInstance(int newPage, Cache cache, String sessionId, Class type, String uniqueId) {
    PagingState state = PagingState.retrieve(cache, sessionId, type, uniqueId);
    return new PageChangeRequest(new Target(state.currentPage(), newPage), state, cache);
  }

  public static PageChangeRequest noPaging() {
    return NO_PAGING;
  }

}
