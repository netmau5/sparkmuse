package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.AbstractComment;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.Set;
import java.util.List;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class Comments<T extends AbstractComment<T>> {

  private final List<T> comments; //wtf is with ImmutableList on appengine?
  private final ImmutableSet<T> allComments;

  public Comments(List<T> comments) {
    ImmutableSet.Builder<T> builder = ImmutableSet.builder();
    for (T post: comments) {
      builder.add(post);
      builder.addAll(getRepliesOf(post));
    }
    this.allComments = builder.build();
    this.comments = Lists.newArrayList(comments);
  }

  //AccessControlException thrown on GAE when this returned immutablelist...
  public List<T> getComments() {
    return comments;
  }

  public ImmutableSet<T> getAllComments() {
    return allComments;
  }

  public int sizeRootComments() {
    return comments.size();
  }

  public int sizeTotalComments() {
    return allComments.size();
  }

  private static <T extends AbstractComment<T>> Set<T> getRepliesOf(final T post) {
    final Set<T> toReturn = Sets.newHashSet();
    toReturn.addAll(post.getReplies());
    for (final T reply: post.getReplies()) {
      toReturn.addAll(getRepliesOf(reply));
    }
    return toReturn;
  }
}
