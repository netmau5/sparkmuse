package models;

import com.vercer.engine.persist.annotation.Key;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 9, 2010
 */
public class UserApplicationModel {

  @Key public Long id;
  public String userName;
  public String url;

}
