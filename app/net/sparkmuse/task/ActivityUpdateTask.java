package net.sparkmuse.task;

import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.data.twig.DatastoreUtils;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.CacheKeyFactory;
import net.sparkmuse.mail.*;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import com.google.inject.Inject;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Query;
import com.google.common.collect.Maps;
import play.templates.Template;
import play.templates.TemplateLoader;
import play.Logger;

import java.util.Map;
import java.util.List;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class ActivityUpdateTask extends Task<Post> {

  private final ObjectDatastore datastore;
  private final MailService mailService;

  private final Cache cache;

  @Inject
  public ActivityUpdateTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, MailService mailService) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
    this.mailService = mailService;

    this.cache = cache;
  }

  protected String getTaskName() {
    return "Activity Update Task";
  }

  @Override
  protected FindCommand.RootFindCommand<Post> find(boolean isNew) {
    final Migration lastMigration = lastMigration();
    final DateTime lastMigrationStart = null != lastMigration ? lastMigration.getStarted() : new DateTime().minusDays(1);
    return datastore.find().type(Post.class)
        .addFilter("created", Query.FilterOperator.GREATER_THAN, lastMigrationStart.getMillis())
        .fetchNextBy(50);
  }

  protected Post transform(Post post) {
    final SparkVO spark = getSpark(post);

    //if someone posted to my spark
    final UserProfile sparkAuthorProfile = getUserProfile(spark.getAuthor());
    if (sparkAuthorProfile.hasEmail() && !isSamePerson(post.getAuthor(), spark.getAuthor())) {
      Logger.debug("Sending SparkActivityUpdate to [" + sparkAuthorProfile.getEmail() + "]");
      final ActivityUpdate update = ActivityUpdate.newSparkActivityUpdate(sparkAuthorProfile, spark, post);
      mailService.prepareAndSendMessage(update);
    }

    if (null != post.getInReplyToId()) {

      //if someone replied to my post
      final Post parentPost = datastore.load(Post.class, post.getInReplyToId());
      final UserProfile parentPostAuthorProfile = getUserProfile(parentPost.getAuthor());
      if (parentPostAuthorProfile.hasEmail() &&
          !isSamePerson(parentPost.getAuthor(), post.getAuthor()) && //a reply to my own post
          !isSamePerson(parentPost.getAuthor(), spark.getAuthor())) { //updatee is not Spark author (already got update above)
        Logger.debug("Sending PostActivityUpdate to [" + parentPostAuthorProfile.getEmail() + "]");
        final ActivityUpdate update = ActivityUpdate.newPostActivityUpdate(parentPostAuthorProfile, spark, post);
        mailService.prepareAndSendMessage(update);
      }

      //person has replied to a post you replied to
      final List<Post> siblings = datastore.find().type(Post.class)
          .addFilter("inReplyToId", Query.FilterOperator.EQUAL, post.getInReplyToId())
          .fetchNextBy(50)
          .returnAll()
          .now();
      for (Post sibling: siblings) {
        //decide if we want to email a sibling post's author
        if (!isSamePerson(sibling.getAuthor(), post.getAuthor()) && //my own post
            !isSamePerson(sibling.getAuthor(), spark.getAuthor()) && //updatee is not Spark author (already got update above)
            !isSamePerson(sibling.getAuthor(), parentPost.getAuthor())) { //updatee is not root post author (already got update above)
          final UserProfile siblingPostAuthorProfile = getUserProfile(sibling.getAuthor());
          if (siblingPostAuthorProfile.hasEmail()) {
            Logger.debug("Sending PostActivityUpdate to [" + siblingPostAuthorProfile + "]");
            final ActivityUpdate update = ActivityUpdate.newSiblingPostActivityUpdate(siblingPostAuthorProfile, spark, post);
            mailService.prepareAndSendMessage(update);
          }
        }
      }
    }

    return post;
  }

  private static boolean isSamePerson(UserVO u1, UserVO u2) {
    return StringUtils.equals(u1.getUserName(), u2.getUserName());
  }

  private UserProfile getUserProfile(UserVO userVO) {
    String profileKey = CacheKeyFactory.newUserKey(userVO.getId()) + "|Profile";

    UserProfile userProfile = cache.get(profileKey, UserProfile.class);
    if (null != userProfile) return userProfile;

    userProfile = datastore.find().type(UserProfile.class)
        .ancestor(DatastoreUtils.associate(userVO, datastore))
        .returnAll()
        .now()
        .get(0);
    cache.set(profileKey, userProfile);

    return userProfile;
  }

  private SparkVO getSpark(Post post) {
    SparkVO spark = cache.get(CacheKeyFactory.newSparkKey(post.getSparkId()));
    if (null != spark) return spark;

    spark = datastore.load(SparkVO.class, post.getSparkId());
    cache.set(spark);
    return spark;
  }

}
