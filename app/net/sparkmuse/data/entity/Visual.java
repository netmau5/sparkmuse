package net.sparkmuse.data.entity;

import play.data.validation.Required;

/**
 * @author neteller
 * @created: Dec 26, 2010
 */
public class Visual extends Entity<Visual> {

  @Required
  private String blobKey;

  @Required
  private String title;

  public String getBlobKey() {
    return blobKey;
  }

  public String getTitle() {
    return title;
  }

  public void setBlobKey(String blobKey) {
    this.blobKey = blobKey;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
