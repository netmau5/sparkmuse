package net.sparkmuse.common;

import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.user.Votable;

import com.google.common.collect.Ordering;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import java.util.List;
import java.util.Collection;

/**
 * @author neteller
 * @created: Jan 7, 2011
 */
public class Orderings {

  public static List<Post> sort(Collection<Post> posts) {
    final ByRecency recency = new ByRecency();
    return sortReplies(Lists.newArrayList(recency.sortedCopy(posts)), reverse(recency));
  }

  private static List<Post> sortReplies(List<Post> posts, Ordering ordering) {
    for (final Post post: posts) {
      post.setReplies(ordering.sortedCopy(post.getReplies()));
      sortReplies(post.getReplies(), ordering);
    }
    return posts;
  }

  public static <T> Ordering reverse(Ordering<T> ordering) {
    return new Reversed<T>(ordering);
  }

  private static class Reversed<T> extends Ordering<T> {

    private final Ordering ordering;

    private Reversed(Ordering ordering) {
      this.ordering = ordering;
    }

    public int compare(T t, T t1) {
      return this.ordering.compare(t1, t);
    }
  }

  public static class ByRecency<T extends Entity & Dateable> extends Ordering<T> {
    public int compare(T a, T b) {
      final int compareTo = b.getCreated().compareTo(a.getCreated());
      return tiebreak(a, b, compareTo);
    }
  }

  public static class ByPostCount extends Ordering<SparkVO> {
    public int compare(SparkVO a, SparkVO b) {
      final int postCount = b.getPostCount() - a.getPostCount();
      return tiebreak(a, b, postCount);
    }
  }

  public static class ByRating extends Ordering<SparkVO> {
    public int compare(SparkVO a, SparkVO b) {
      final int rating = ((int) b.getRating() * 100) - ((int) a.getRating() * 100);
      return tiebreak(a, b, rating);
    }
  }

  public static class ByVotes extends Ordering<Votable> {
    public int compare(Votable votable, Votable votable1) {
      return -1 * Ints.compare(votable.getVotes(), votable1.getVotes()); //-1 for descending order
    }
  }

  private static int tiebreak(Entity a, Entity b, int compareTo) {
    if (0 != compareTo) return compareTo;
    else return a.getId().compareTo(b.getId());
  }
  
}
