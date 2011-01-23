package net.sparkmuse.mail;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.UserProfile;

/**
 * @author neteller
 * @created: Jan 23, 2011
 */
public class PostActivityUpdate implements ActivityUpdate {

  private final UserProfile updatee; //updatee who did the action one should be notified of
  private final SparkVO spark; //spark where this occurred
  private final Post post; //update

  public PostActivityUpdate(UserProfile updatee, SparkVO spark, Post post) {
    this.updatee = updatee;
    this.spark = spark;
    this.post = post;
  }

  public String getTemplate() {
    return "Mail/PostActivityUpdate.html";
  }

  public String getToEmail() {
    return updatee.getEmail();
  }

  public String getSubject() {
    return getName() + " replied to your post";
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
