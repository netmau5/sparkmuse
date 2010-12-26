package net.sparkmuse.data;

import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.SparkVO;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface PostDao {
  Collection<Post> findPostsBySpark(SparkVO spark);
}
