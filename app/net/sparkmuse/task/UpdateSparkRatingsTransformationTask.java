package net.sparkmuse.task;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Cursor;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.SparkRanking;
import net.sparkmuse.common.Cache;
import play.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 7, 2010
 */
public class UpdateSparkRatingsTransformationTask extends TransformationTask<SparkVO> {

  private final Cache cache;
  private final ObjectDatastore datastore;

  @Inject
  public UpdateSparkRatingsTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.cache = cache;
    this.datastore = datastore;
  }

  protected SparkVO transform(SparkVO sparkVO) {
    sparkVO.setRating(SparkRanking.calculateRating(sparkVO));
    return sparkVO;
  }

  protected FindCommand.RootFindCommand<SparkVO> find(boolean isNew) {
    return datastore.find().type(SparkVO.class);
  }

  @Override
  protected void onEnd() {
    cache.clear(); //@todo just clear spark stuff.
  }
}
