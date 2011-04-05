package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Parent;
import com.google.code.twig.annotation.Embedded;
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


}
