package net.sparkmuse.data.twig;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import com.google.inject.Inject;
import com.google.common.collect.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.sparkmuse.data.entity.PostVO;
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

  public Collection<PostVO> findPostsBySpark(final SparkVO spark) {
    return applyHierarchy(helper.all(
        datastore.find().type(PostVO.class).addFilter("sparkId", EQUAL, spark.getId())
    ));
  }


  private Collection<PostVO> applyHierarchy(final List<PostVO> posts) {
    ImmutableMap<Long, PostVO> postById = Maps.uniqueIndex(posts, new Function<PostVO, Long>() {
      public Long apply(PostVO post) {
        return post.getId();
      }
    });

    //append any post with an inReplyToId property as a reply to its parent
    for (final PostVO post: posts) {
      if (null != post.getInReplyToId()) {
        PostVO parent = postById.get(post.getInReplyToId());
        parent.setReplies(ImmutableList.<PostVO>builder().addAll(parent.getReplies()).add(post).build());
      }
    }

    //posts are traversed recursively (getReplies), remove any non-root posts from the returned list
    return Lists.newArrayList(Iterables.filter(posts, new Predicate<PostVO>(){
      public boolean apply(PostVO post) {
        return null == post.getInReplyToId();
      }
    }));
  }
}
