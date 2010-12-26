package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.Post;
import net.sparkmuse.user.Votable;

import java.util.List;
import java.util.Collection;

import com.google.common.collect.Ordering;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class VoteSorter {

  private static final VoteComparator voteOrdering = new VoteComparator();

  public static List<Post> sortPosts(Collection<Post> posts) {
    return sortReplies(Lists.newArrayList(voteOrdering.sortedCopy(posts)));
  }

  private static List<Post> sortReplies(List<Post> posts) {
    for (final Post post: posts) {
      post.setReplies(ImmutableList.copyOf(voteOrdering.sortedCopy(post.getReplies())));
      sortReplies(post.getReplies());
    }
    return posts;
  }

  public static List<Votable> sort(Collection<Votable> votables) {
    return Lists.newArrayList(voteOrdering.sortedCopy(votables));
  }

  private static class VoteComparator extends Ordering<Votable> {
    public int compare(Votable votable, Votable votable1) {
      return -1 * Ints.compare(votable.getVotes(), votable1.getVotes()); //-1 for descending order
    }
  }
}
