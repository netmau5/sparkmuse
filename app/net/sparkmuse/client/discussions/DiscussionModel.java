package net.sparkmuse.client.discussions;

import net.sparkmuse.data.entity.Discussion;
import net.sparkmuse.data.entity.UserProfile;
import net.sparkmuse.user.UserVotes;
import net.sparkmuse.common.Templates;
import net.sparkmuse.common.Template;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * @author neteller
 * @created: Apr 9, 2011
 */
public class DiscussionModel {

  private Long id;
  private String html;
  private Long created;
  private String title;

  public DiscussionModel() {

  }

  public Long getCreated() {
    return created;
  }

  public String getHtml() {
    return html;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public static DiscussionModel newInstance(Discussion discussion, UserVotes userVotes, UserProfile userProfile) {
    DiscussionModel model = new DiscussionModel();

    model.id = discussion.getId();
    model.html = Templates.render(new DiscussionTemplate(discussion, userVotes, userProfile));
    model.created = discussion.getCreated().getMillis();
    model.title = discussion.getTitle();

    return model;
  }

  private static class DiscussionTemplate implements Template {

    private final Map<String, Object> arguments;

    DiscussionTemplate(Discussion discussion, UserVotes userVotes, UserProfile userProfile) {
      this.arguments = ImmutableMap.of(
          "_discussion", discussion,
          "_userVotes", userVotes,
          "_userProfile", userProfile
      );
    }

    public String getTemplate() {
      return "tags/discuss/discussionInfo.html";
    }

    public Map<String, Object> getTemplateArguments() {
      return this.arguments;
    }
  }
}
