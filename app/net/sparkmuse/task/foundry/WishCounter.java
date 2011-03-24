package net.sparkmuse.task.foundry;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * @author neteller
 * @created: Mar 23, 2011
 */
public class WishCounter implements Serializable {

  private Map<String, Integer> tagCount;

  WishCounter() {
    this.tagCount = Maps.newHashMap();
  }

  public Map<String, Integer> getTagCount() {
    return tagCount;
  }

  public void increment(String tag) {
    Integer tagCount = this.tagCount.get(tag);
    if (null == tagCount) {
      this.tagCount.put(tag, 1);
    }
    else {
      this.tagCount.put(tag, tagCount + 1);
    }
  }
}