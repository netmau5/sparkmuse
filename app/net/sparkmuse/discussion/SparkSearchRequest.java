package net.sparkmuse.discussion;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public class SparkSearchRequest {

  public enum Filter {
    RECENT,
    POPULAR,
    DISCUSSED,
    TAGGED
  }

  private final Filter filter;

  private String tag;

  public SparkSearchRequest(Filter filter) {
    this.filter = filter;
  }

  public static SparkSearchRequest forTag(final String tag) {
    SparkSearchRequest request = new SparkSearchRequest(Filter.TAGGED);
    request.tag = tag;
    return request;
  }

  public Filter getFilter() {
    return filter;
  }

  public String getTag() {
    return tag;
  }
}
