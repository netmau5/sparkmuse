package net.sparkmuse.data.twig;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import com.google.appengine.api.datastore.Query;
import com.google.inject.Inject;
import com.google.common.collect.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.PostDao;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Sep 20, 2010
 */
public class TwigPostDao extends TwigDao implements PostDao {

  @Inject
  public TwigPostDao(DatastoreService service) {
    super(service);
  }

  public Collection<Post> findPostsBySpark(final SparkVO spark) {
    return applyHierarchy(helper.all(
        datastore.find().type(Post.class).addFilter("sparkId", EQUAL, spark.getId())
    ));
  }

  public List<Post> findSiblings(Post post) {
    if (null == post.getInReplyToId()) return null;
    return datastore.find().type(Post.class)
        .addFilter("inReplyToId", Query.FilterOperator.EQUAL, post.getInReplyToId())
        .fetchNextBy(50)
        .returnAll()
        .now();
  }

  private Collection<Post> applyHierarchy(final List<Post> posts) {
    ImmutableMap<Long, Post> postById = Maps.uniqueIndex(posts, new Function<Post, Long>() {
      public Long apply(Post post) {
        return post.getId();
      }
    });

    //append any post with an inReplyToId property as a reply to its parent
    for (final Post post: posts) {
      if (null != post.getInReplyToId()) {
        Post parent = postById.get(post.getInReplyToId());
        if (!parent.getReplies().contains(post)) {
          parent.setReplies(ImmutableList.<Post>builder().addAll(parent.getReplies()).add(post).build());
        }
      }
    }

    //posts are traversed recursively (getReplies), remove any non-root posts from the returned list
    return Lists.newArrayList(Iterables.filter(posts, new Predicate<Post>(){
      public boolean apply(Post post) {
        return null == post.getInReplyToId();
      }
    }));
  }
}
