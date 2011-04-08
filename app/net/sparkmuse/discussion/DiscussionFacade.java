package net.sparkmuse.discussion;

import net.sparkmuse.data.DiscussionDao;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.discussion.NewDiscussionEmbedTask;
import net.sparkmuse.common.Cache;
import net.sparkmuse.common.Orderings;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.user.Votable;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.embed.Embed;
import com.google.inject.Inject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Set;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class DiscussionFacade {

  private final DiscussionDao discussionDao;
  private final Cache cache;
  private final IssueTaskService issueTaskService;
  private final UserFacade userFacade;

  @Inject
  public DiscussionFacade(UserFacade userFacade, DiscussionDao discussionDao, Cache cache, IssueTaskService issueTaskService) {
    this.discussionDao = discussionDao;
    this.cache = cache;
    this.issueTaskService = issueTaskService;
    this.userFacade = userFacade;
  }

  public DiscussionsResponse findRecentDiscussions(UserVO requestingUser) {
    List<Discussion> discussions = discussionDao.findMostRecentDiscussions();
    Set<Votable> votables = Sets.<Votable>newHashSet(discussions);
    UserVotes votes = userFacade.findUserVotesFor(votables, requestingUser);
    return new DiscussionsResponse(discussions, votes);
  }

  public DiscussionResponse getDiscussionBy(Long discussionId, UserVO requestingUser) {
    Discussion discussion = findDiscussionBy(discussionId);
    DiscussionContent content = findDiscussionContentBy(discussionId);
    Comments comments = findCommentsFor(discussionId);

    Set<Votable> votables = Sets.<Votable>newHashSet(comments.getAllComments());
    votables.add(discussion);

    UserVotes votes = userFacade.findUserVotesFor(votables, requestingUser);

    return new DiscussionResponse(
        discussion,
        content,
        comments,
        votes
    );
  }

  public Discussion storeDiscussion(final Discussion discussion) {
    boolean isNew = null == discussion.getId();

    if (!isNew) {
      discussion.setEdited(new DateTime());
    }

    final Discussion newDiscussion = discussionDao.store(discussion);

    //author implicitly votes for discussion; thus, they will not be able to vote for it again
    userFacade.recordUpVote(newDiscussion, newDiscussion.getAuthor().getId());

    if (isNew) {
      newDiscussion.getAuthor().issueIncrementTask(issueTaskService, UserVO.Statistic.DISCUSSION);
    }

    issueTaskService.issue(NewDiscussionEmbedTask.class, ImmutableMap.of(
        NewDiscussionEmbedTask.PARAMETER_DISCUSSION_ID, newDiscussion.getId()
    ), null);

    return newDiscussion;
  }

  public DiscussionComment createComment(final DiscussionComment comment) {
    Preconditions.checkArgument(null == comment.getId());
    final DiscussionComment newComment = discussionDao.store(comment);

    //modify post count
    final Discussion discussion = findDiscussionBy(comment.getDiscussionId());
    discussion.setCommentCount(discussion.getCommentCount() + 1);
    discussionDao.store(discussion);
    newComment.getAuthor().issueIncrementTask(issueTaskService, UserVO.Statistic.POST);

    //author implicitly votes for post; thus, they will not be able to vote for it again
    userFacade.recordUpVote(newComment, newComment.getAuthor().getId());

    return newComment;
  }

  private DiscussionContent findDiscussionContentBy(Long discussionId) {
    return discussionDao.findDiscussionContentBy(discussionId);
  }

  private DiscussionContent findOrCreateDiscussionContent(Discussion discussion) {
    DiscussionContent content = findDiscussionContentBy(discussion.getId());
    if (null == content) {
      return DiscussionContent.fromNewDiscussion(discussion);
    }
    else {
      return content;
    }
  }

  public Discussion findDiscussionBy(Long discussionId) {
    return discussionDao.findDiscussionBy(discussionId);
  }

  private Comments findCommentsFor(Long discussionId) {
    return new Comments(Orderings.sort(discussionDao.findCommentsFor(discussionId)));
  }

  public void addEmbed(Discussion discussion, Embed embed) {
    DiscussionContent content = findOrCreateDiscussionContent(discussion);
    content.setEmbed(embed);
    discussionDao.store(content);
  }
}
