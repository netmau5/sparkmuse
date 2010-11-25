package controllers;

import net.sparkmuse.user.UserFacade;
import net.sparkmuse.data.entity.UserVO;

import javax.inject.Inject;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 24, 2010
 */
public class User extends SparkmuseController {

  @Inject static UserFacade userFacade;

  public static void view(Long userId) {
    final UserVO user = userFacade.findUserBy(userId);
    render(user);
  }

}
