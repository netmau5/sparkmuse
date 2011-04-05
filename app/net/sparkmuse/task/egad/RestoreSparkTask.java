package net.sparkmuse.task.egad;

import net.sparkmuse.data.entity.Activity;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.twig.DatastoreService;
import net.sparkmuse.data.twig.DatastoreUtils;
import net.sparkmuse.common.Cache;
import net.sparkmuse.task.TransformationTask;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Query;
import com.google.inject.Inject;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;

import java.util.List;

import play.Logger;

/**
 * @author neteller
 * @created: Apr 3, 2011
 */
public class RestoreSparkTask extends TransformationTask<Activity> {

  private static final String CACHE_KEY_CORRECT_SPARK_COUNTER = "CACHE_KEY_CORRECT_SPARK_COUNTER";

  private final ObjectDatastore datastore;
  private final DatastoreService datastoreService;
  private final Cache cache;

  @Inject
  public RestoreSparkTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, DatastoreService datastoreService) {
    super(cache, batchService, datastore);
    this.datastoreService = datastoreService;
    this.datastore = datastore;
    this.cache = cache;
  }

  protected Activity transform(final Activity activity) {
    Long activitySparkId = activity.getSummary().getSparkId();

    FindCommand.RootFindCommand<SparkVO> findCommand = datastore.find().type(SparkVO.class)
        .addFilter("title", Query.FilterOperator.EQUAL, activity.getSummary().getSparkTitle());
    List<SparkVO> sparks = Lists.newArrayList();

    try {
      sparks = datastoreService.all(findCommand);
    }
    catch(IllegalStateException e) {
      Logger.error(e, "Could not translate sparks for [" + activity.getSummary().getSparkTitle() + "]");
      return null;
    }

    if (sparks.size() == 1 && sparks.get(0).getId().equals(activity.getContentKey())) {
      Logger.info("Spark [" + sparks.get(0).getTitle() + "] has the correct id [" + activity.getContentKey() + "]");
      incrementCorrectCount();
      updateSpark(sparks.get(0));
    }
    else if (sparks.size() == 0) {
      Logger.error("Could not find Spark [" + activity.getSummary().getSparkTitle() + "] with id [" + activitySparkId + "]");
    }
    else if (sparks.size() > 1) {
      Logger.error("Found more than one Spark [" + activity.getSummary().getSparkTitle() + "]");
      List<SparkVO> correctSparks = Lists.newArrayList(Iterables.filter(sparks, new Predicate<SparkVO>() {
        public boolean apply(SparkVO o) {
          return (o.getId().equals(activity.getSummary().getSparkId()));
        }
      }));
      if (correctSparks.size() == 1) {
        Logger.info("Found correct spark [" + correctSparks.get(0) + "]");
        updateSpark(correctSparks.get(0));
      }
      else if (correctSparks.size() == 0) {
        Logger.info("Of the [" + sparks.size() + "] sparks found with the title [" + activity.getSummary().getSparkTitle() + "], none had the proper ID");
        //remove one from sparks, give it the proper id, and store it
        SparkVO spark = sparks.remove(0);
        datastore.delete(spark);
        spark.setId(activitySparkId);
        storeSpark(spark);
      }
      else if (correctSparks.size() > 1) {
        Logger.info("Of the [" + sparks.size() + "] sparks found with the title [" + activity.getSummary().getSparkTitle() + "], more than one had the proper ID");
        //remove one from correct sparks, give it the proper id, and store it
        SparkVO spark = correctSparks.remove(0);
        datastore.delete(spark);
        spark.setId(activitySparkId);
        storeSpark(spark);
        //delete the rest
        datastore.deleteAll(correctSparks);
      }

      List<SparkVO> incorrectSparks = Lists.newArrayList(Iterables.filter(sparks, new Predicate<SparkVO>() {
        public boolean apply(SparkVO o) {
          return !(o.getId().equals(activity.getSummary().getSparkId()));
        }
      }));
      if (incorrectSparks.size() > 0) {
        Logger.info("Found [" + incorrectSparks.size() + "] incorrect sparks");
        datastore.deleteAll(incorrectSparks);
      }
    }
    else {
      Logger.error("Found only one Spark [" + activity.getSummary().getSparkTitle() + "], but id from activity [" + activity.getContentKey() + "] didn't match spark's id [" + sparks.get(0).getId() + "]");
      SparkVO spark = sparks.get(0);
      SparkVO existingSpark = datastore.load(SparkVO.class, activitySparkId);
      if (null != existingSpark) {
        //move it to a new id
        Logger.error("Attempted to correct invalid spark id, but another spark already exists for activity's spark id [" + activitySparkId + "] with title [" + existingSpark.getTitle() + "]");
        datastore.delete(existingSpark);
        existingSpark.setId(null);
        datastore.store(existingSpark);
      }
      datastore.delete(spark);
      spark.setId(activitySparkId);
      storeSpark(spark);
    }


    return null;
  }

  private void storeSpark(SparkVO spark) {
    spark.setCorrection(1);
    DatastoreUtils.store(spark, datastore);
  }

  private void updateSpark(SparkVO spark) {
    spark.setCorrection(1);
    DatastoreUtils.update(spark, datastore);
  }

  private void incrementCorrectCount() {
    Integer count = cache.get(CACHE_KEY_CORRECT_SPARK_COUNTER, Integer.class);
    if (null == count) {
      cache.set(CACHE_KEY_CORRECT_SPARK_COUNTER, new Integer(1));
    }
    else {
      cache.set(CACHE_KEY_CORRECT_SPARK_COUNTER, new Integer(count + 1));
    }
  }

  @Override
  protected void onBegin() {
    cache.delete(CACHE_KEY_CORRECT_SPARK_COUNTER);
  }

  @Override
  protected void onEnd() {
    Logger.info("[" + cache.get(CACHE_KEY_CORRECT_SPARK_COUNTER, Integer.class) + "] Sparks were found in the correct state.");
  }

  protected FindCommand.RootFindCommand<Activity> find(boolean isNew) {
    return datastore.find().type(Activity.class)
        .addFilter("population", Query.FilterOperator.EQUAL, Activity.Population.EVERYONE.toString())
        .addFilter("kind", Query.FilterOperator.EQUAL, Activity.Kind.SPARK.toString());
  }
}
