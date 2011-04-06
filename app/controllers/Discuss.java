package controllers;

import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.data.entity.DiscussionComment;
import net.sparkmuse.discussion.DiscussionFacade;
import net.sparkmuse.discussion.DiscussionsResponse;
import net.sparkmuse.discussion.DiscussionResponse;
import play.data.validation.Valid;

import javax.inject.Inject;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class Discuss extends SparkmuseController {

  @Inject
  static DiscussionFacade discussionFacade;

  public static void index() {
    DiscussionsResponse discussionsResponse = discussionFacade.findRecentDiscussions(Authorization.getUserFromSession());
    render(discussionsResponse);
  }

  public static void view(Long discussionId) {
    DiscussionResponse discussionResponse = discussionFacade.getDiscussionBy(discussionId, Authorization.getUserFromSession());
    render(discussionResponse);
  }

  public static void submit(@Valid Discussion discussion) {

  }

  public static void reply(DiscussionComment comment) {

  }

}
