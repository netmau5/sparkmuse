package net.sparkmuse.mail;

import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.Post;

/**
 * @author neteller
 * @created: Jan 30, 2011
 */
public class SiblingPostActivityUpdate extends PostActivityUpdate {

  public SiblingPostActivityUpdate(UserProfile updatee, SparkVO spark, Post post) {
    super(updatee, spark, post);
  }

  @Override
  public String getSubject() {
    return getName() + " replied to a post that you replied to.";
  }

}
