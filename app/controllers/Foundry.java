package controllers;

import controllers.SparkmuseController;
import controllers.Authorization;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.discussion.FoundryFacade;
import net.sparkmuse.discussion.WishSearchResponse;
import net.sparkmuse.common.Cache;

import javax.inject.Inject;

/**
 * @author neteller
 * @created: Mar 10, 2011
 */
public class Foundry extends SparkmuseController {

  @Inject static FoundryFacade foundryFacade;
  @Inject static Cache cache;

  public static void index(int page) {
    UserVO user = Authorization.getUserFromSession();
    WishSearchResponse wishSearchResponse = foundryFacade.findTaggedWishes(user, PageChangeRequest.newInstance(page, cache, user, Wish.class, "Wish"));
    render(wishSearchResponse);
  }

  public static void tagged(int page) {
    UserVO user = Authorization.getUserFromSession();
    WishSearchResponse wishSearchResponse = foundryFacade.findTaggedWishes(user, PageChangeRequest.newInstance(page, cache, user, Wish.class, "TaggedWish"));
    render(wishSearchResponse);
  }

  public static void create() {
    render();
  }

}
