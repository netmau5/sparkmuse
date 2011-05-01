package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.paging.PageChangeRequest;

/**
 * @author neteller
 * @created: Apr 30, 2011
 */
public class WishSearchRequest {

  public enum Filter {
    TAGGED,
    RECENT,
    POPULAR
  }

  private final UserVO user;
  private final Filter filter;
  private final PageChangeRequest pageChangeRequest;

  private String tagName;

  private WishSearchRequest(UserVO user, Filter filter, PageChangeRequest pageChangeRequest) {
    this.user = user;
    this.filter = filter;
    this.pageChangeRequest = pageChangeRequest;
  }

  public UserVO getUser() {
    return user;
  }

  public Filter getFilter() {
    return filter;
  }

  public String getTagName() {
    return tagName;
  }

  public PageChangeRequest getPageChangeRequest() {
    return pageChangeRequest;
  }

  public static WishSearchRequest newSearch(UserVO user, Filter filter, PageChangeRequest pageChangeRequest) {
    WishSearchRequest request = new WishSearchRequest(user, filter, pageChangeRequest);
    return request;
  }

  public static WishSearchRequest newTagSearch(UserVO user, String tagName, PageChangeRequest pageChangeRequest) {
    WishSearchRequest request = new WishSearchRequest(user, Filter.TAGGED, pageChangeRequest);
    request.tagName = tagName;
    return request;
  }



}
