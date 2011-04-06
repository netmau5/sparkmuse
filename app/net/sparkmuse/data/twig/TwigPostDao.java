package net.sparkmuse.data.twig;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import com.google.appengine.api.datastore.Query;
import com.google.inject.Inject;
import com.google.common.collect.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.AbstractComment;
import net.sparkmuse.data.PostDao;
import net.sparkmuse.discussion.Posts;

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
    return AbstractComment.applyHierarchy(helper.all(
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

  public void deleteAll(Posts posts) {
    datastore.deleteAll(posts.getResources());
    datastore.deleteAll(posts.getVisuals());
    datastore.deleteAll(posts.getOffers());
    datastore.deleteAll(posts.getAllComments());
  }
}
