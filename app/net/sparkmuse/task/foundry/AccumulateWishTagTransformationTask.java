package net.sparkmuse.task.foundry;

import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.entity.TagCount;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.task.TransformationTask;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.*;
import java.io.Serializable;

/**
 * @author neteller
 * @created: Mar 23, 2011
 */
public class AccumulateWishTagTransformationTask extends TransformationTask<Wish> {

  private static final String COUNTER_NAME = AccumulateWishTagTransformationTask.class.getName() + "|Counter";

  private Cache cache;
  private ObjectDatastore datastore;

  @Inject
  public AccumulateWishTagTransformationTask(BatchDatastoreService batchService, ObjectDatastore datastore, Cache cache) {
    super(cache, batchService, datastore);
    this.cache = cache;
    this.datastore = datastore;
  }

  @Override
  protected void onBegin() {
    super.onBegin();
    cache.set(COUNTER_NAME, new WishCounter());
  }

  @Override
  protected void onEnd() {
    super.onEnd();

    final Map<String, Integer> tagCount = getCounter().getTagCount();
    List<String> tags = Lists.newArrayList(tagCount.keySet());

    Collections.sort(tags, new Comparator<String>(){
      public int compare(String s, String s1) {
        return tagCount.get(s1) - tagCount.get(s);
      }
    });
    
    datastore.store(new TagCount(TagCount.NAME_WISH_TAG_COUNTER, tags.size() > 10 ? tags.subList(0, 10) : tags));
  }

  protected Wish transform(Wish wish) {
    WishCounter counter = getCounter();

    for (String tag: wish.getTags()) {
      counter.increment(tag);
    }

    cache.set(COUNTER_NAME, counter);

    return wish;
  }

  private WishCounter getCounter() {
    WishCounter counter = cache.get(COUNTER_NAME, WishCounter.class);

    if (null == counter) throw new IllegalStateException("Counter removed from cache, must restart Wish tag accumulation.");
    return counter;
  }

  protected FindCommand.RootFindCommand<Wish> find(boolean isNew) {
    return datastore.find().type(Wish.class);
  }

}
