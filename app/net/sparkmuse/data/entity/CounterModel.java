package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Id;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Collection;

/**
 * @author neteller
 * @created: Mar 22, 2011
 */
public class CounterModel {

  //used as key, includes counter name, shard number, and any other uniquely identifying info
  @Id
  private String key;

  //used as queryable key, includes everything in key without shard
  private String queryKey;

  private int count;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getQueryKey() {
    return queryKey;
  }

  public void setQueryKey(String queryKey) {
    this.queryKey = queryKey;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public static String keyFor(String name, int shard, Object... identifiers) {
    return createKeyBase(name, identifiers) + "|Shard:" + shard;
  }

  public static String querykeyFor(String name, Object... identifiers) {
    return createKeyBase(name, identifiers);
  }

  private static String createKeyBase(String name, Object[] identifiers) {
    StringBuilder sb = new StringBuilder(name);
    for (Object identifier: identifiers) {
      sb.append("|").append(identifier.toString());
    }
    return sb.toString();
  }


  private static int generateShardNumber(int maxShards) {
    double v = Math.random();
    return ((int) v * maxShards) + 1;
  }

  public static CounterModel singleInstance(String name, int maxShards, Object identifier) {
    CounterModel counterModel = new CounterModel();
    counterModel.key = CounterModel.keyFor(name, generateShardNumber(maxShards), identifier);
    counterModel.queryKey = CounterModel.querykeyFor(name, identifier);
    return counterModel;
  }

  public static Collection<CounterModel> allInstances(String name, int maxShards, Object identifier) {
    List<CounterModel> toReturn = Lists.newArrayList();

    for (int i = 1; i <= maxShards; i++) {
      CounterModel counterModel = new CounterModel();
      counterModel.key = CounterModel.keyFor(name, i, identifier);
      counterModel.queryKey = CounterModel.querykeyFor(name, identifier);
      toReturn.add(counterModel);
    }

    return toReturn;
  }


}
