package models;

import com.vercer.engine.persist.annotation.Key;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class VoteModel {

  @Key
  public Long id;
  public int voteWeight; //should generally be -1, 0, +1
  public String entityClassName;
  public Long entityId;
  public Long userId;

}
