package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.Cacheable;
import net.sparkmuse.common.CacheKey;

import java.util.List;
import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Mar 29, 2011
 */
public class TopWishes implements Serializable, Cacheable<TopWishes> {

  public static final CacheKey<TopWishes> CACHE_KEY = new CacheKey(TopWishes.class);

  private final List<Wish> wishes;
  private final DateTime expireDate;

  public TopWishes(List<Wish> wishes) {
    this.wishes = wishes;
    this.expireDate = new DateTime().plusHours(2);
  }

  public List<Wish> getWishes() {
    return wishes;
  }

  public CacheKey<TopWishes> getKey() {
    return CACHE_KEY;
  }

  public TopWishes getInstance() {
    return this;
  }

  public boolean hasExpired() {
    return new DateTime().isAfter(this.expireDate);
  }

  public boolean isEmpty() {
    return this.wishes.size() == 0;
  }
}
