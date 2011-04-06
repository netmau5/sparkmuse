package net.sparkmuse.common;

import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.AbstractComment;
import net.sparkmuse.user.Votable;

import com.google.common.collect.Ordering;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import java.util.List;
import java.util.Collection;
import java.io.Serializable;

/**
 * @author neteller
 * @created: Jan 7, 2011
 */
public class Orderings {

  private static abstract class SerializableOrdering<T> extends Ordering<T> implements Serializable {}

  public static <T extends AbstractComment> List<T> sort(Collection<T> posts) {
    final ByRecency recency = new ByRecency();
    return sortReplies(Lists.newArrayList(recency.sortedCopy(posts)), reverse(recency));
  }

  private static <T extends AbstractComment> List<T> sortReplies(List<T> posts, Ordering ordering) {
    for (final T post: posts) {
      post.setReplies(ordering.sortedCopy(post.getReplies()));
      sortReplies(post.getReplies(), ordering);
    }
    return posts;
  }

  public static <T> Ordering reverse(Ordering<T> ordering) {
    return new Reversed<T>(ordering);
  }

  private static class Reversed<T> extends SerializableOrdering<T> {

    private final Ordering ordering;

    private Reversed(Ordering ordering) {
      this.ordering = ordering;
    }

    public int compare(T t, T t1) {
      return this.ordering.compare(t1, t);
    }
  }

  public static class ByRecency<T extends Entity & Dateable> extends SerializableOrdering<T> {
    public int compare(T a, T b) {
      final int compareTo = b.getCreated().compareTo(a.getCreated());
      return tiebreak(a, b, compareTo);
    }
  }

  public static class ByPostCount extends SerializableOrdering<SparkVO> {
    public int compare(SparkVO a, SparkVO b) {
      final int postCount = b.getPostCount() - a.getPostCount();
      return tiebreak(a, b, postCount);
    }
  }

  public static class ByRating extends SerializableOrdering<SparkVO> {
    public int compare(SparkVO a, SparkVO b) {
      final double rating = (b.getRating() * 100) - (a.getRating() * 100);
      return tiebreak(a, b, (int) (10000 * rating));
    }
  }

  public static class ByVotes extends SerializableOrdering<Votable> {
    public int compare(Votable votable, Votable votable1) {
      return -1 * Ints.compare(votable.getVotes(), votable1.getVotes()); //-1 for descending order
    }
  }

  public static class ByVotesEntry<T extends Entity> extends SerializableOrdering<Entry<Post, T>> {
    public int compare(Entry<Post, T> entry, Entry<Post, T> entry1) {
      final int comparedVotes = Ints.compare(entry.getKey().getVotes(), entry1.getKey().getVotes());
      return tiebreak(entry.getValue(), entry1.getValue(), comparedVotes);
    }
  }

  private static int tiebreak(Entity a, Entity b, int compareTo) {
    if (0 != compareTo) return compareTo;
    else return a.getId().compareTo(b.getId());
  }
  
}
