package net.sparkmuse.data.twig;

import com.google.inject.Inject;
import com.google.common.base.Function;
import com.google.code.twig.ObjectDatastore;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Entity;
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
    return helper.mergeOwnerFor(helper.load(SparkVO.class, id));
  }

  public SparkVO create(final SparkVO spark) {
    return helper.store(spark);
  }

  public List<SparkVO> loadPopular() {
    return helper.mergeOwnersFor(helper.all(SparkVO.class, datastore.find()
        .type(Entity.modelClassFor(SparkVO.class))
        .addSort("rating")
        .fetchMaximum(50)
    ));
  }

  public String transform(final Function<SparkVO, SparkVO> transformation, final String cursor) {
    return super.transformAll(SparkVO.class, transformation, cursor);
  }

}
