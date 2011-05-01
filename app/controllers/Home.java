package controllers;

import play.mvc.With;
import filters.AuthorizationFilter;
import net.sparkmuse.discussion.*;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.Votables;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.data.paging.PagingState;
import net.sparkmuse.common.Cache;
import net.sparkmuse.ajax.AjaxResponse;

import javax.inject.Inject;
import java.util.TreeSet;
import java.util.Set;

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
  @Inject static FoundryFacade foundryFacade;
  @Inject static UserFacade userFacade;
  @Inject static Cache cache;

  public static void index() {
    search(SparkSearchRequest.Filter.RECENT, 1);
  }

  public static void search(SparkSearchRequest.Filter filter, int page) {
    UserVO currentUser = Authorization.getUserFromSession();
    PageChangeRequest pageChangeRequest = PageChangeRequest.newInstance(page, cache, session.getId(), SparkVO.class, filter.toString());
    PagingState pagingState = pageChangeRequest.getState(); //@todo this gets transitioned in facade; updated state on object by reference = :(
    SparkSearchResponse sparkSearch = sparkFacade.search(new SparkSearchRequest(filter, pageChangeRequest));

    Set<SparkVO> sparks = sparkSearch.getSparks(pagingState);
    final UserVotes userVotes = userFacade.findUserVotesFor(Votables.collect(sparkSearch), currentUser);
    TopWishes topWishes = foundryFacade.getTopWishes();
    render(sparks, topWishes, filter, pagingState, userVotes);
  }

  public static void tagged(String tagged) {
    SparkSearchResponse sparkSearch = sparkFacade.search(SparkSearchRequest.forTag(tagged, PageChangeRequest.noPaging()));
    final UserVotes userVotes = userFacade.findUserVotesFor(Votables.collect(sparkSearch), Authorization.getUserFromSession());
    final SparkAssets assets = new SparkAssets(sparkSearch);
    render(sparkSearch, assets, userVotes, tagged);
  }

  public static void welcome() {
    final UserVO user = Authorization.getUserFromSession();
    render(user);
  }

}
