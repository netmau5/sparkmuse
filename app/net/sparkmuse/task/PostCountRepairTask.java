package net.sparkmuse.task;

import net.sparkmuse.common.Cache;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import net.sparkmuse.data.twig.BatchDatastoreService;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Cursor;
import com.google.common.base.Function;
import play.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Reports and repairs the comment counts on Sparks.
 *
 * @author neteller
 * @created: Jan 17, 2011
 */
public class PostCountRepairTask extends TransformationTask<SparkVO> {

  private final Cache cache;
  private final ObjectDatastore datastore;

  @Inject
  public PostCountRepairTask(Cache cache, ObjectDatastore datastore, BatchDatastoreService batch) {
    super(cache, batch, datastore);
    this.cache = cache;
    this.datastore = datastore;
  }

  protected SparkVO transform(SparkVO sparkVO) {
    final Integer count = datastore.find()
        .type(Post.class)
        .addFilter("sparkId", EQUAL, sparkVO.getId())
        .returnCount()
        .now();

    if (sparkVO.getPostCount() != count) {
      Logger.error("Spark [" + sparkVO.getTitle() + "] reported a post count of [" + sparkVO.getPostCount() + "] but had [" + count + "] posts.");
      sparkVO.setPostCount(count);
    }

    return sparkVO;
  }

  protected FindCommand.RootFindCommand<SparkVO> find(boolean isNew) {
    return datastore.find().type(SparkVO.class);
  }

  @Override
  protected void onEnd() {
    cache.clear();
  }

}
