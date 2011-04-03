package net.sparkmuse.mail;

import net.sparkmuse.common.Template;
import net.sparkmuse.data.entity.UserVO;

import java.util.Map;
import java.util.List;

import com.google.common.collect.ImmutableMap;

/**
 * @author neteller
 * @created: Apr 3, 2011
 */
public class TopContributorsTemplate implements Template {

  private final List<UserVO> topContributors;

  public TopContributorsTemplate(List<UserVO> topContributors) {
    this.topContributors = topContributors;
  }

  public String getTemplate() {
    return "Mail/TopContributors.html";
  }

  public Map<String, Object> getTemplateArguments() {
    return ImmutableMap.<String, Object>of("topContributors", topContributors);
  }
}
