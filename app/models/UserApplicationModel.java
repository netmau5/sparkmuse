package models;

import com.google.code.twig.annotation.Id;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 9, 2010
 */
public class UserApplicationModel {

  @Id public Long id;
  public String userName;
  public String url;

}
