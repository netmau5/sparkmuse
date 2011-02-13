package net.sparkmuse.activity;

import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.DaoProvider;
import net.sparkmuse.mail.ActivityUpdate;
import net.sparkmuse.mail.MailService;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.common.Cache;
import play.Logger;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.google.inject.Inject;

/**
 * Produces activity notifications for new events.
 *
 * @author neteller
 * @created: Feb 11, 2011
 */
public class ActivityService {

  public static final String GLOBAL_ACTIVITY = "GLOBAL_ACTIVITY";

  private final DaoProvider daoProvider;
  private final Cache cache;
  private final MailService mailService;

  @Inject
  public ActivityService(Cache cache, MailService mailService, DaoProvider daoProvider) {
    this.cache = cache;
    this.mailService = mailService;
    this.daoProvider = daoProvider;
  }

  public ActivityStream getActivity(UserVO user) {
    ActivityStream globalActivity = getGlobalActivity();
    ActivityStream userActivity = ActivityStream.builder(daoProvider.getActivityDao())
        .forUser(user)
        .after(globalActivity.getOldestTime())
        .build();
    return globalActivity.overlay(userActivity);
  }

  private ActivityStream getGlobalActivity() {
    ActivityStream everyone = cache.get(GLOBAL_ACTIVITY, ActivityStream.class);
    if (null == everyone) {
      everyone = ActivityStream.builder(daoProvider.getActivityDao()).build();
      cache.set(GLOBAL_ACTIVITY, everyone);
    }
    return everyone;
  }

  public void notify(SparkVO newSpark) {
    store(Activity.newSparkActivity(newSpark), newSpark);
    store(Activity.newUserSparkActivity(newSpark), newSpark);
    
    newSpark.setNotified(true);
    daoProvider.getSparkDao().store(newSpark);
  }

  public void notify(Post newPost) {
    if (newPost.isNotified()) return;

    final SparkVO spark = getSpark(newPost);

    store(Activity.newPostActivity(spark, newPost), newPost);
    store(Activity.newUserPostActivity(spark, newPost), newPost);

    //if someone posted to my spark
    final UserProfile sparkAuthorProfile = getUserProfile(spark.getAuthor());
    if (!isSamePerson(newPost.getAuthor(), spark.getAuthor())) {
      store(Activity.newSparkPostActivity(spark, newPost), newPost);

      if (sparkAuthorProfile.hasEmail()) {
        Logger.debug("Sending SparkActivityUpdate to [" + sparkAuthorProfile.getEmail() + "]");
        final ActivityUpdate update = ActivityUpdate.newSparkActivityUpdate(sparkAuthorProfile, spark, newPost);
        mailService.prepareAndSendMessage(update);
      }
    }

    if (null != newPost.getInReplyToId()) {

      //if someone replied to my post
      final Post parentPost = daoProvider.getPostDao().load(Post.class, newPost.getInReplyToId());
      final UserProfile parentPostAuthorProfile = getUserProfile(parentPost.getAuthor());
      if (!isSamePerson(parentPost.getAuthor(), newPost.getAuthor()) && //a reply to my own post
          !isSamePerson(parentPost.getAuthor(), spark.getAuthor())) { //updatee is not Spark author (already got update above)
        store(Activity.newReplyPostActivity(spark, parentPost, newPost), newPost);

        if (parentPostAuthorProfile.hasEmail()) {
          Logger.debug("Sending PostActivityUpdate to [" + parentPostAuthorProfile.getEmail() + "]");
          final ActivityUpdate update = ActivityUpdate.newPostActivityUpdate(parentPostAuthorProfile, spark, newPost);
          mailService.prepareAndSendMessage(update);
        }
      }

      //person has replied to a post you replied to
      final List<Post> siblings = daoProvider.getPostDao().findSiblings(newPost);
      for (Post sibling: siblings) {
        //decide if we want to email a sibling newPost's author
        if (!isSamePerson(sibling.getAuthor(), newPost.getAuthor()) && //my own newPost
            !isSamePerson(sibling.getAuthor(), spark.getAuthor()) && //updatee is not Spark author (already got update above)
            !isSamePerson(sibling.getAuthor(), parentPost.getAuthor())) { //updatee is not root post author (already got update above)
          final UserProfile siblingPostAuthorProfile = getUserProfile(sibling.getAuthor());

          store(Activity.newSiblingReplyPostActivity(spark, sibling, newPost), newPost);

          if (siblingPostAuthorProfile.hasEmail()) {
            Logger.debug("Sending PostActivityUpdate to [" + siblingPostAuthorProfile + "]");
            final ActivityUpdate update = ActivityUpdate.newSiblingPostActivityUpdate(siblingPostAuthorProfile, spark, newPost);
            mailService.prepareAndSendMessage(update);
          }
        }
      }
    }

    newPost.setNotified(true);
    daoProvider.getPostDao().store(newPost);
  }

  private void store(Activity activity, Notifiable notifiable) {
    if (!notifiable.isNotified()) {
      //@todo
      //in the future we will need to check for overlap.  right now Source.PERSONAL and Source.REPLY
      //cannot overlap, but that will change with the implementation of Source.FOLLOWER
      daoProvider.getActivityDao().store(activity);
      cache.delete(GLOBAL_ACTIVITY);
    }
  }

  private static boolean isSamePerson(UserVO u1, UserVO u2) {
    return StringUtils.equals(u1.getUserName(), u2.getUserName());
  }

  private UserProfile getUserProfile(UserVO userVO) {
    String profileKey = CacheKeyFactory.newUserKey(userVO.getId()) + "|Profile";

    UserProfile userProfile = cache.get(profileKey, UserProfile.class);
    if (null != userProfile) return userProfile;

    userProfile = daoProvider.getUserDao().findUserProfileBy(userVO.getUserName());
    cache.set(profileKey, userProfile);

    return userProfile;
  }

  private SparkVO getSpark(Post post) {
    SparkVO spark = cache.get(CacheKeyFactory.newSparkKey(post.getSparkId()));
    if (null != spark) return spark;
    spark = daoProvider.getSparkDao().findById(post.getSparkId());
    cache.set(spark);
    return spark;
  }

}
