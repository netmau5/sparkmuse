package net.sparkmuse.task;

import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.twig.BatchDatastoreService;
import net.sparkmuse.common.Cache;
import net.sparkmuse.mail.ActivityUpdate;
import net.sparkmuse.mail.SparkActivityUpdate;
import net.sparkmuse.mail.MailService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import com.google.inject.Inject;
import com.google.code.twig.FindCommand;
import com.google.code.twig.ObjectDatastore;
import com.google.appengine.api.datastore.Query;
import com.google.common.collect.Maps;
import play.templates.Template;
import play.templates.TemplateLoader;

import java.util.Map;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class ActivityUpdateTask extends Task<Post> {

  private final ObjectDatastore datastore;
  private final MailService mailService;

  @Inject
  public ActivityUpdateTask(Cache cache, BatchDatastoreService batchService, ObjectDatastore datastore, MailService mailService) {
    super(cache, batchService, datastore);
    this.datastore = datastore;
    this.mailService = mailService;
  }

  protected String getTaskName() {
    return "Activity Update Task";
  }

  @Override
  protected FindCommand.RootFindCommand<Post> find(boolean isNew) {
    final Migration lastMigration = lastMigration();
    final DateTime lastMigrationStart = null != lastMigration ? lastMigration.getStarted() : new DateTime();
    return datastore.find().type(Post.class)
        .addFilter("created", Query.FilterOperator.GREATER_THAN, lastMigrationStart)
        .fetchNextBy(50);
  }

  protected Post transform(Post post) {
    //if someone posted to my spark
    final SparkVO spark = getSpark(post);
    final UserProfile sparkAuthorProfile = getUserProfile(spark.getAuthor());
    if (StringUtils.isNotBlank(sparkAuthorProfile.getEmail())) { //sparkAuthorProfile.isSendActivityUpdates() && 
      final SparkActivityUpdate update = new SparkActivityUpdate(sparkAuthorProfile, spark, post);
      mailService.sendMessage(prepareEmail(update));
    }
    return post;
  }

  private UserProfile getUserProfile(UserVO userVO) {
    return datastore.find().type(UserProfile.class)
        .ancestor(userVO)
        .returnAll()
        .now()
        .get(0);
  }

  private SparkVO getSpark(Post post) {
    return datastore.load(SparkVO.class, post.getSparkId());
  }

  private Email prepareEmail(ActivityUpdate update) {
    final Template template = TemplateLoader.load(update.getTemplate());
    final Map<String, Object> args = Maps.newHashMap();
    args.put("update", update);
    final String content = template.render(args);

    try {
      HtmlEmail email = new HtmlEmail();
//      email.addTo(update.getToEmail());
      email.addTo("dave@sparkmuse.com");
      email.setFrom("noreply@sparkmuse.com", "Sparkmuse");
      email.setSubject(update.getSubject());
      email.setHtmlMsg(content);
      return email;
    } catch (EmailException e) {
      throw new RuntimeException(e);
    }
  }

}
