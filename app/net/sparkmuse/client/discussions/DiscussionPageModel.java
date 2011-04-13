package net.sparkmuse.client.discussions;

import net.sparkmuse.discussion.DiscussionsResponse;
import net.sparkmuse.data.entity.DiscussionGroup;
import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.data.entity.UserProfile;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;

/**
 * @author neteller
 * @created: Apr 9, 2011
 */
public class DiscussionPageModel {

  private DiscussionGroup group;
  private List<DiscussionModel> discussions;

  public DiscussionGroup getGroup() {
    return group;
  }

  public void setGroup(DiscussionGroup group) {
    this.group = group;
  }

  public List<DiscussionModel> getDiscussions() {
    return discussions;
  }

  public void setDiscussions(List<DiscussionModel> discussions) {
    this.discussions = discussions;
  }

  public static DiscussionPageModel newInstance(final DiscussionsResponse discussionsResponse, final UserProfile userProfile) {
    DiscussionPageModel model = new DiscussionPageModel();
    model.group = null != discussionsResponse.getGroup() ? discussionsResponse.getGroup() : DiscussionGroup.GENERAL_DISCUSSION;
    model.discussions = Lists.newArrayList(Iterables.transform(discussionsResponse.getDiscussions(), new Function<Discussion, DiscussionModel>(){
      public DiscussionModel apply(Discussion discussion) {
        return DiscussionModel.newInstance(discussion, discussionsResponse.getUserVotes(), userProfile);
      }
    }));
    return model;
  }

}
