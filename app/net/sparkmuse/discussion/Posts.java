package net.sparkmuse.discussion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.common.Orderings;
import net.sparkmuse.common.Entry;
import net.sparkmuse.common.Entries;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Aggregate collection of Post objects.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class Posts {

  private final SparkVO spark;
  private final ImmutableList<Post> posts;

  private final List<Entry<Post, Resource>> resources;
  private final List<Entry<Post, Visual>> visuals;
  private final List<Entry<Post, Offer>> offers;

  public Posts(SparkVO spark, List<Post> posts) {
    this.spark = spark;
    this.posts = ImmutableList.copyOf(posts);

    final List<Entry<Post, Resource>> resources = Lists.newArrayList();
    final List<Entry<Post, Visual>> visuals = Lists.newArrayList();
    final List<Entry<Post, Offer>> offers = Lists.newArrayList();

    for (final Post post: posts) {
      resources.addAll(toEntries(post, post.getResources()));
      visuals.addAll(toEntries(post, post.getVisuals()));
      offers.addAll(toEntries(post, post.getOffers()));
    }

    final Orderings.ByVotesEntry orderingByVotes = new Orderings.ByVotesEntry();
    this.resources = orderingByVotes.sortedCopy(resources);
    this.visuals = orderingByVotes.sortedCopy(visuals);
    this.offers = orderingByVotes.sortedCopy(offers);
  }

  public <T> Collection<Entry<Post, T>> toEntries(final Post post, Iterable<T> items) {
    final Function<T, Entry<Post, T>> toEntries = new Function<T, Entry<Post, T>>() {
      public Entry<Post, T> apply(T t) {
        return new Entry(post, t);
      }
    };

    return Lists.newArrayList(Iterables.transform(items, toEntries));
  }

  public ImmutableList<Post> getPosts() {
    return posts;
  }

  public int countRootPosts() {
    return posts.size();
  }

  public int countTotalPosts() {
    int size = countRootPosts();
    for (final Post post: posts) {
      size += countRepliesOf(post);
    }
    return size;
  }

  private static int countRepliesOf(final Post post) {
    int size = post.getReplies().size();
    for (final Post reply: post.getReplies()) {
      size += countRepliesOf(reply);
    }
    return size;
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
