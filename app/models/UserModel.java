package models;

import com.google.code.twig.annotation.Id;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 4, 2010
 */
public class UserModel {

  @Id public Long id;
  public String userName;
  public String userId;
  public int reputation;
  public String authorization;

}
