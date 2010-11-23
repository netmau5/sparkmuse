package models;

import com.google.code.twig.annotation.Id;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class VoteModel {

  @Id
  public String key; //should be [entityClassName]|[entityId]
  public int voteWeight; //should generally be -1, 0, +1
  public String entityClassName;
  public Long entityId;

}
