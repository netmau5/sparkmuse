package net.sparkmuse.data.entity;

import play.data.validation.Required;
import play.data.validation.URL;
import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Jan 6, 2011
 */
public class Resource extends Entity<Resource> {

  public enum ResourceType {
    VALIDATION,
    SIMILAR_IDEA,
    RESEARCH
  }

  @Required
  @URL
  private String url;

  @Required
  private String title;

  @Required
  private ResourceType type;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    if (StringUtils.isBlank(title)) return url;
    else return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ResourceType getType() {
    return type;
  }

  public void setType(ResourceType type) {
    this.type = type;
  }
  
}
