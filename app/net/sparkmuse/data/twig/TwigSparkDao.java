package net.sparkmuse.data.twig;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.inject.Inject;
import com.google.common.base.Function;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.FindCommand;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.SparkDao;
import net.sparkmuse.data.Cache;
import net.sparkmuse.common.TimedTransformer;
import models.SparkModel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Sep 20, 2010
 */
public class TwigSparkDao extends TwigDao implements SparkDao {

  @Inject
  public TwigSparkDao(ObjectDatastore datastore, ObjectMapper map) {
    super(datastore, map);
  }

  public SparkVO findById(final Long id) {
    //a simple test commit
    return helper.load(SparkVO.class, id);
  }

  public SparkVO create(final SparkVO spark) {
    return helper.store(spark);
  }

  public List<SparkVO> loadPopular() {
    return helper.all(SparkVO.class, datastore.find()
        .type(Entity.modelClassFor(SparkVO.class))
        .addSort("rating")
        .maximumResults(50)
    );
  }

  public String transform(final Function<SparkVO, SparkVO> transformation, final String cursor) {
    return super.transformAll(SparkVO.class, transformation, cursor);
  }

}
