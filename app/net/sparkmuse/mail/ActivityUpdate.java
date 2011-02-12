package net.sparkmuse.mail;

import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author neteller
 * @created: Feb 5, 2011
 */
public class ActivityUpdate implements EmailTemplate {

  private final UserProfile updatee; //user who should be updated
  private final SparkVO spark; //spark where this occurred
  private final Post post; //update

  private String subTemplate;
  private String subject;

  public ActivityUpdate(UserProfile updatee, SparkVO spark, Post post) {
    this.updatee = updatee;
    this.spark = spark;
    this.post = post;
  }

  public static ActivityUpdate newSparkActivityUpdate(UserProfile updatee, SparkVO spark, Post post) {
    ActivityUpdate update = new ActivityUpdate(
        updatee,
        spark,
        post
    );
    update.subTemplate = "Mail/SparkActivityUpdate.html";
    update.subject = update.getName() + " posted on your Spark";
    return update;
  }

  public static ActivityUpdate newPostActivityUpdate(UserProfile updatee, SparkVO spark, Post post) {
    ActivityUpdate update = new ActivityUpdate(
        updatee,
        spark,
        post
    );
    update.subTemplate = "Mail/PostActivityUpdate.html";
    update.subject = update.getName() + " replied to your post";
    return update;
  }

  public static ActivityUpdate newSiblingPostActivityUpdate(UserProfile updatee, SparkVO spark, Post post) {
    ActivityUpdate update = newPostActivityUpdate(updatee, spark, post);
    update.subject = update.getName() + " replied to a post that you replied to";
    return update;
  }

  public String getTemplate() {
    return "Mail/ActivityUpdate.html";
  }

  public Map<String, Object> getTemplateArguments() {
    final Map<String, Object> args = Maps.newHashMap();
    args.put("update", this);
    return args;
  }

  public String getToEmail() {
    return updatee.getEmail();
  }

  public String getSubject() {
    return subject;
  }

  public String getUpdateeName() {
    return StringUtils.isEmpty(updatee.getName()) ? updatee.getUser().getUserName() : updatee.getName();
  }

  //used in template

  public String getSubTemplate() {
    return subTemplate;
  }

  public String getName() {
    return post.getAuthor().getUserName();
  }

  public SparkVO getSpark() {
    return spark;
  }

  public Post getPost() {
    return post;
  }

}
