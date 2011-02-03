package controllers;

import play.mvc.With;
import play.Play;
import filters.AuthorizationFilter;
import net.sparkmuse.discussion.SparkSearchResponse;
import net.sparkmuse.discussion.SparkFacade;
import net.sparkmuse.discussion.SparkSearchRequest;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.Votables;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.UserProfile;

import javax.inject.Inject;
import java.util.List;

/**
 * Controller for the home page.  The home page is where
 * the user will see a list of popular and new Sparks.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
@With(AuthorizationFilter.class)
public class Home extends SparkmuseController {

  @Inject static SparkFacade sparkFacade;
  @Inject static UserFacade userFacade;

  public static void index() {
    search(SparkSearchRequest.Filter.RECENT);
  }

  public static void search(SparkSearchRequest.Filter filter) {
    SparkSearchResponse sparkSearch = sparkFacade.search(filter);
    final UserVotes userVotes = userFacade.findUserVotesFor(Votables.collect(sparkSearch), Authorization.getUserFromSession());
    render(sparkSearch, userVotes);
  }

  public static void welcome() {
    final UserVO user = Authorization.getUserFromSession();
    render(user);
  }

  public static void people() {
    if (Play.mode == Play.Mode.DEV) {
      List<UserProfile> profiles = userFacade.getAllProfiles();
      render(profiles);
    }
    index();
  }

}
