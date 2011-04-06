package net.sparkmuse.discussion;

import com.google.common.collect.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.common.Orderings;
import net.sparkmuse.common.Entry;
import net.sparkmuse.common.Entries;

import play.Logger;

import java.util.List;
import java.util.Collection;
import java.util.Set;

/**
 * Aggregate collection of Post objects.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Posts extends Comments<Post> {

  private final SparkVO spark;

  private final List<Entry<Post, Resource>> resources;
  private final List<Entry<Post, Visual>> visuals;
  private final List<Entry<Post, Offer>> offers;

  public Posts(SparkVO spark, List<Post> posts) {
    super(posts);
    this.spark = spark;

    List<Entry<Post, Resource>> resources = collectResources(posts);
    final List<Entry<Post, Visual>> visuals = collectVisuals(posts);
    final List<Entry<Post, Offer>> offers = collectOffers(posts);

    final Orderings.ByVotesEntry orderingByVotes = new Orderings.ByVotesEntry();
    this.resources = orderingByVotes.sortedCopy(resources);
    this.visuals = orderingByVotes.sortedCopy(visuals);
    this.offers = orderingByVotes.sortedCopy(offers);
  }

  private List<Entry<Post, Resource>> collectResources(Iterable<Post> posts) {
    List<Entry<Post, Resource>> resources = Lists.newArrayList();
    for (final Post post: posts) {
      resources.addAll(toEntries(post, post.getResources()));
      resources.addAll(collectResources(post.getReplies()));
    }
    return resources;
  }

  private List<Entry<Post, Visual>> collectVisuals(Iterable<Post> posts) {
    List<Entry<Post, Visual>> resources = Lists.newArrayList();
    for (final Post post: posts) {
      resources.addAll(toEntries(post, post.getVisuals()));
      resources.addAll(collectVisuals(post.getReplies()));
    }
    return resources;
  }

  private List<Entry<Post, Offer>> collectOffers(Iterable<Post> posts) {
    List<Entry<Post, Offer>> resources = Lists.newArrayList();
    for (final Post post: posts) {
      resources.addAll(toEntries(post, post.getOffers()));
      resources.addAll(collectOffers(post.getReplies()));
    }
    return resources;
  }

  public <T> Collection<Entry<Post, T>> toEntries(final Post post, Iterable<T> items) {
    final Function<T, Entry<Post, T>> toEntries = new Function<T, Entry<Post, T>>() {
      public Entry<Post, T> apply(T t) {
        return new Entry(post, t);
      }
    };

    return Lists.newArrayList(Iterables.transform(items, toEntries));
  }

  public SparkVO getSpark() {
    return spark;
  }

  public List<Resource> getSimilarIdeas() {
    return getResources(Resource.ResourceType.SIMILAR_IDEA);
  }

  public List<Resource> getValidations() {
    return getResources(Resource.ResourceType.VALIDATION);
  }

  public List<Resource> getResearches() {
    return getResources(Resource.ResourceType.RESEARCH);
  }

  public List<Resource> getResources() {
    return Entries.values(resources);
  }

  public List<Resource> getResources(final Resource.ResourceType type) {
    return Lists.newArrayList(Iterables.filter(getResources(), new Predicate<Resource>(){
      public boolean apply(Resource resource) {
        return type.equals(resource.getType());
      }
    }));
  }

  public List<Visual> getVisuals() {
    return Entries.values(visuals);
  }

  public List<Entry<Post, Offer>> getOffers() {
    return offers;
  }
}
