package net.sparkmuse.user;

import net.sparkmuse.discussion.SparkSearchResponse;
import net.sparkmuse.discussion.Posts;
import net.sparkmuse.data.entity.SparkVO;

import java.util.Set;
import java.util.HashSet;

import com.google.common.collect.Sets;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public class Votables {

  public static String newKey(Votable votable) {
    return votable.getClass().getName() + "|" + votable.getId();
  }

  public static Set<Votable> collect(SparkSearchResponse response) {
    final Iterable<Set<Votable>> votableSets = Iterables.transform(response.getSparks(), new Function<SparkVO, Set<Votable>>() {
      public Set<Votable> apply(SparkVO sparkVO) {
        return collect(sparkVO);
      }
    });
    final Iterable<Votable> votables = Iterables.concat(votableSets);
    return Sets.newHashSet(votables);
  }

  public static Set<Votable> collect(SparkVO spark) {
    return Sets.<Votable>newHashSet(spark);
  }

  public static Set<Votable> collect(SparkVO spark, Posts posts) {
    final HashSet<Votable> voteables = Sets.<Votable>newHashSet(spark);
    voteables.addAll(posts.getAllPosts());
    return voteables;
  }

}
