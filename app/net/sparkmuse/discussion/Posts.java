package net.sparkmuse.discussion;

import com.google.common.collect.ImmutableList;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.SparkVO;

import java.util.List;

/**
 * Aggregate collection of Post objects.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Posts {

  private final SparkVO spark;
  private final ImmutableList<Post> posts;

  public Posts(SparkVO spark, List<Post> posts) {
    this.spark = spark;
    this.posts = ImmutableList.copyOf(posts);
  }

  public ImmutableList<Post> getPosts() {
    return posts;
  }

  public int countRootPosts() {
    return posts.size();
  }

  public int countTotalPosts() {
    int size = countRootPosts();
    for (final Post post: posts) {
      size += countRepliesOf(post);
    }
    return size;
  }

  private static int countRepliesOf(final Post post) {
    int size = post.getReplies().size();
    for (final Post reply: post.getReplies()) {
      size += countRepliesOf(reply);
    }
    return size;
  }

}
