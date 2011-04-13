package net.sparkmuse.common;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.paging.PagingState;
import net.sparkmuse.discussion.PopularSparks;
import net.sparkmuse.discussion.RecentSparks;
import net.sparkmuse.discussion.MostDiscussedSparks;
import net.sparkmuse.discussion.DiscussionGroups;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class CacheKeyFactory {

  public static CacheKey<UserVO> newUserKey(final Long id) {
    return new CacheKey(UserVO.class, id);
  }

  public static CacheKey<SparkVO> newSparkKey(final Long id) {
    return new CacheKey(SparkVO.class, id);
  }

  public static CacheKey<UserProfile> newUserProfileKey(final Long id) {
    return new CacheKey(UserProfile.class, id);
  }

  public static CacheKey<PopularSparks> newPopularSparksKey() {
    return new CacheKey(PopularSparks.class);
  }

  public static CacheKey<RecentSparks> newRecentSparksKey() {
    return new CacheKey(RecentSparks.class);
  }

  public static CacheKey<MostDiscussedSparks> newMostDiscussedSparksKey() {
    return new CacheKey(MostDiscussedSparks.class);
  }

  /**
   * @param user          user this paging state belongs to
   * @param type          type of entity being paged through
   * @param uniqueId      nullable; if there is more than one paging mechanism applied to this object type,
   *                      this is distinguishes (ie, multiple types of entity results to be paged through)
   * @return
   */
  public static CacheKey<PagingState> newPagingState(UserVO user, Class type, String uniqueId){
    return PagingState.newKey(user, type, uniqueId);
  }

  public static CacheKey<DiscussionGroups> newDiscussionGroupsKey() {
    return new CacheKey(DiscussionGroups.class);
  }

}
