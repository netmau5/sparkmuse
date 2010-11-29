package models;

import com.google.code.twig.annotation.Id;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class PostModel {

  @Id public Long id;
  public Long inReplyToId;
  public Long sparkId;

  public long created;
  public long edited;

  public int votes;
  public String postContent;

}
