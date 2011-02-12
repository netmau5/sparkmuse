package net.sparkmuse.data;

import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.SparkVO;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface PostDao extends CrudDao {
  Collection<Post> findPostsBySpark(SparkVO spark);
  List<Post> findSiblings(Post post); //returns null if this post isn't a reply
}
