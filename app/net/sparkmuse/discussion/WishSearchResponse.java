package net.sparkmuse.discussion;

import net.sparkmuse.user.UserVotes;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.paging.PagingState;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
public class WishSearchResponse {

  private final UserVotes userVotes;
  private final List<Wish> wishes;
  private final List<String> topTags;
  private final PagingState pagingState;

  public WishSearchResponse(List<Wish> wishes, UserVotes userVotes, PagingState pagingState) {
    this(wishes, userVotes, Lists.<String>newArrayList(), pagingState);
  }

  public WishSearchResponse(List<Wish> wishes, UserVotes userVotes, List<String> topTags, PagingState pagingState) {
    this.userVotes = userVotes;
    this.wishes = wishes;
    this.pagingState = pagingState;
    this.topTags = topTags;
  }

  public UserVotes getUserVotes() {
    return userVotes;
  }

  public List<Wish> getWishes() {
    return wishes;
  }

  public PagingState getPagingState() {
    return pagingState;
  }

  public List<String> getTopTags() {
    return topTags;
  }
}
