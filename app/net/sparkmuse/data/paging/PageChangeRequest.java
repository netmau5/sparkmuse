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

  public static PageChangeRequest NO_PAGING = new PageChangeRequest(Target.SAME, null, null);

  private final Target target;
  private final PagingState state;
  private final Cache cache;

  public enum Target {
    PREVIOUS(-1),
    SAME(0),
    NEXT(1);

    int modifier;

    Target(int modifier) {
      this.modifier = modifier;
    }

    static Target determine(int currentPage, int newPage) {
      if (currentPage < newPage) return NEXT;
      else if (currentPage > newPage) return PREVIOUS;
      else return SAME;
    }
  }

  PageChangeRequest(Target target, PagingState state, Cache cache) {
    this.target = target;
    this.state = state;
    this.cache = cache;
  }

  public Target getTarget() {
    return target;
  }

  public PagingState getState() {
    return state;
  }

  public <T> FindCommand.RootFindCommand<T> applyPaging(FindCommand.RootFindCommand<T> find) {
    if (this == NO_PAGING) return find;

    int newPage = this.state.currentPage() + this.target.modifier;
    Cursor cursor = state.cursorBeforePage(newPage);
    int pageSize = this.state.pageSize();

    find.fetchMaximum(pageSize + 1);
    find.fetchFirst(pageSize + 1);

    if (null != cursor) {
      find.continueFrom(cursor);
    }
    else {
      find.startFrom(this.state.currentPage() * pageSize);
    }

    return find;
  }

  public int maxResultIndex() {
    if (NO_PAGING == this) return 999;
    return this.state.pageSize() * (this.target.modifier + this.state.currentPage());
  }

  public void transition(boolean hasNext, Cursor cursor) {
    if (NO_PAGING == this) return;

    if (Target.NEXT == this.target) {
      this.state.increment(cursor, hasNext);
    }
    else if (Target.PREVIOUS == this.target) {
      this.state.decrement();
    }
    else {
      this.state.same(hasNext);
    }
    this.cache.set(this.state);
  }

  /**
   * Creates a new PageChangeRequest.  The existing page state is retrieved from the cache or initialized at
   * page 1.
   *
   * @param newPage       page requested
   * @param cache
   * @param currentUser
   * @param type          underlying object type that is being paged
   * @param uniqueId      nullable; if there is more than one paging mechanism applied to this object type,
   *                      this is distinguishes (ie, multiple types of entity results to be paged through)
   * @return
   */
  public static PageChangeRequest newInstance(int newPage, Cache cache, UserVO currentUser, Class type, String uniqueId) {
    PagingState state = PagingState.retrieve(cache, currentUser, type, uniqueId);
    return new PageChangeRequest(Target.determine(state.currentPage(), newPage), state, cache);
  }

  public static PageChangeRequest noPaging() {
    return NO_PAGING;
  }

}
