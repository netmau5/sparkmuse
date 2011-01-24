package net.sparkmuse.mail;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.UserProfile;
import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class SparkActivityUpdate implements ActivityUpdate {

  private final UserProfile updatee; //user who should be updated
  private final SparkVO spark; //spark where this occurred
  private final Post post; //update

  public SparkActivityUpdate(UserProfile updatee, SparkVO spark, Post post) {
    this.updatee = updatee;
    this.spark = spark;
    this.post = post;
  }

  public String getTemplate() {
    return "Mail/SparkActivityUpdate.html";
  }

  public String getToEmail() {
    return updatee.getEmail();
  }

  public String getSubject() {
    return getName() + " posted on your Spark";
  }

  public String getUpdateeName() {
    return StringUtils.isEmpty(updatee.getName()) ? updatee.getUser().getUserName() : updatee.getName();
  }

  //used in template

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
