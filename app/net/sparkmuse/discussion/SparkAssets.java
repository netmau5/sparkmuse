package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;

import java.util.List;
import java.util.TreeSet;

import com.google.common.collect.Lists;

/**
 * Break down of resources, visuals, and people associated to a group
 * of Sparks.
 *
 * @author neteller
 * @created: Feb 17, 2011
 */
public class SparkAssets {

  private final List<UserVO> users;

  public SparkAssets(SparkSearchResponse response) {
    this.users = collectUsers(response.getSparks());
  }

  private static List<UserVO> collectUsers(TreeSet<SparkVO> sparks) {
    List<UserVO> users = Lists.newArrayList();
    for (SparkVO spark: sparks) {
      users.add(spark.getAuthor());
    }
    return users;
  }

  public List<UserVO> getUsers() {
    return users;
  }
}
