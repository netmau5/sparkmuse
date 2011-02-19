package net.sparkmuse.discussion;

import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.common.Orderings;
import com.google.common.collect.Ordering;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public class SparkSearchRequest {

  public enum Filter {
    RECENT(new Orderings.ByRecency()),
    POPULAR(new Orderings.ByRating()),
    DISCUSSED(new Orderings.ByPostCount()),
    TAGGED(new Orderings.ByRecency());

    private final Ordering ordering;

    Filter(Ordering ordering) {
      this.ordering = ordering;
    }

    public Ordering getOrdering() {
      return ordering;
    }
  }

  private final Filter filter;
  private final PageChangeRequest pageChangeRequest;

  private String tag;

  public SparkSearchRequest(Filter filter, PageChangeRequest pageChangeRequest) {
    this.filter = filter;
    this.pageChangeRequest = pageChangeRequest;
  }

  public static SparkSearchRequest forTag(final String tag, PageChangeRequest pageChangeRequest) {
    SparkSearchRequest request = new SparkSearchRequest(Filter.TAGGED, pageChangeRequest);
    request.tag = tag;
    return request;
  }

  public Filter getFilter() {
    return filter;
  }

  public PageChangeRequest getPageChangeRequest() {
    return pageChangeRequest;
  }

  public String getTag() {
    return tag;
  }
}
