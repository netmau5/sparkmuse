package net.sparkmuse.data.entity;

import play.data.validation.Required;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

/**
 * @author neteller
 * @created: Jan 6, 2011
 */
public class Resource extends Entity<Resource> {

  @Required
  private String url;

  @Required
  private String title;

  @Type(Text.class)
  private String displayDescription;

  @Type(Text.class)
  private String description;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDisplayDescription() {
    return displayDescription;
  }

  public void setDisplayDescription(String displayDescription) {
    this.displayDescription = displayDescription;
  }
}
