package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Parent;
import com.google.code.twig.annotation.Embedded;
import com.google.common.base.Preconditions;
import net.sparkmuse.embed.Embed;

/**
 * Additional content for a discussion.
 *
 * @author neteller
 * @created: Apr 5, 2011
 */
public class DiscussionContent extends OwnedEntity<DiscussionContent> {

  @Parent
  private Discussion discussion;
  private Long discussionId;

  @Embedded
  private Embed embed;

  public Discussion getDiscussion() {
    return discussion;
  }

  public void setDiscussion(Discussion discussion) {
    this.discussion = discussion;
  }

  public Long getDiscussionId() {
    return discussionId;
  }

  public void setDiscussionId(Long discussionId) {
    this.discussionId = discussionId;
  }

  public Embed getEmbed() {
    return embed;
  }

  public void setEmbed(Embed embed) {
    this.embed = embed;
  }

  public static DiscussionContent fromNewDiscussion(final Discussion discussion) {
    Preconditions.checkNotNull(discussion.getId());

    DiscussionContent content = new DiscussionContent();
    content.discussion =  discussion;
    content.discussionId = discussion.getId();
    return content;
  }

}
