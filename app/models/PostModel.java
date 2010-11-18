package models;

import com.vercer.engine.persist.annotation.Key;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class PostModel {

  @Key public Long id;
  public Long inReplyToId;
  public Long sparkId;

  public long created;
  public long edited;

  public int votes;
  public String postContent;

}
