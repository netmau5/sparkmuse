package net.sparkmuse.task;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;

/**
 * @author neteller
 * @created: Feb 17, 2011
 */
public class LowercaseSparkTagsTransformationTask extends TransformationTask<SparkVO> {

  private final ObjectDatastore datastore;

  public LowercaseSparkTagsTransformationTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
  }

  protected SparkVO transform(SparkVO sparkVO) {
    return sparkVO.lowercaseTags();
  }

  protected FindCommand.RootFindCommand<SparkVO> find(boolean isNew) {
    return datastore.find().type(SparkVO.class);
  }

}
