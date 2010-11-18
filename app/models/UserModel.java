package models;

import com.vercer.engine.persist.annotation.Key;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 4, 2010
 */
public class UserModel {

  @Key public Long id;
  public String userName;
  public String userId;
  public int reputation;
  public String authorization;

}
