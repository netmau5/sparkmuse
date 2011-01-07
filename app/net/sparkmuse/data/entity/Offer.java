package net.sparkmuse.data.entity;

import play.data.validation.Required;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

/**
 * @author neteller
 * @created: Jan 6, 2011
 */
public class Offer extends Entity<Offer> {

  public static enum Expertise {
    BIG_DATA,
    BUSINESS,
    DESIGN,
    DOMAIN_EXPERTISE,
    ENGINEERING,
    FINANCE,
    LEAN_METHODOLOGY,
    MARKETING,
    MOBILE,
    PRODUCT_DEVELOPMENT,
    SALES,
    SEO,
    SOCIAL_MEDIA
  }

  @Required
  private Expertise expertise;

  @Type(Text.class)
  private String message;
  @Type(Text.class)
  private String displayMessage;

  public Expertise getExpertise() {
    return expertise;
  }

  public void setExpertise(Expertise expertise) {
    this.expertise = expertise;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDisplayMessage() {
    return displayMessage;
  }

  public void setDisplayMessage(String displayMessage) {
    this.displayMessage = displayMessage;
  }
}
