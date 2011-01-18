package net.sparkmuse.data.twig;

import com.google.inject.Inject;
import com.google.common.base.Function;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.SparkDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Sep 20, 2010
 */
public class TwigSparkDao extends TwigDao implements SparkDao {

  @Inject
  public TwigSparkDao(DatastoreService service) {
    super(service);
  }

  public SparkVO findById(final Long id) {
    return helper.load(SparkVO.class, id);
  }

  public List<SparkVO> loadPopular() {
    return helper.all(datastore.find()
        .type(SparkVO.class)
        .addSort("rating")
        .fetchMaximum(50)
    );
  }

  public List<SparkVO> loadRecent() {
    return helper.all(datastore.find()
        .type(SparkVO.class)
        .addSort("created")
        .fetchMaximum(50)
    );
  }

  public List<SparkVO> loadMostDiscussed() {
    return helper.all(datastore.find()
        .type(SparkVO.class)
        .addSort("postCount")
        .fetchMaximum(50)
    );
  }

}
