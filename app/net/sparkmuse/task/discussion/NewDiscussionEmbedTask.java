package net.sparkmuse.task.discussion;

import net.sparkmuse.task.Task;
import net.sparkmuse.discussion.DiscussionFacade;
import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.common.DiscussionType;
import net.sparkmuse.embed.Embedly;
import net.sparkmuse.embed.Embed;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import com.google.appengine.api.datastore.Cursor;
import play.Logger;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class NewDiscussionEmbedTask extends Task {

  public static String PARAMETER_DISCUSSION_ID = "PARAMETER_DISCUSSION_ID";

  private final DiscussionFacade discussionFacade;

  @Inject
  public NewDiscussionEmbedTask(ObjectDatastore datastore, DiscussionFacade discussionFacade) {
    super(datastore);
    this.discussionFacade = discussionFacade;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    Discussion discussion = discussionFacade.findDiscussionBy(Long.parseLong(getParameter(PARAMETER_DISCUSSION_ID)));
    if (null != discussion && discussion.getDiscussionType() == DiscussionType.LINK) {
      try {
        Embed embed = Embedly.lookup(discussion.getUrl());
        if (null != embed) discussionFacade.addEmbed(discussion, embed);
      }
      catch(Throwable t) {
        Logger.info(t, "Could not retrieve Embed for Discussion [" + discussion + "].");
      }
    }
    return null;
  }
}
