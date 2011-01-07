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
    MARKETING,
    SALES,
    SEO,
    ENGINEERING,
    DESIGN,
    BIG_DATA,
    PRODUCT_DEVELOPMENT,
    SOCIAL_MEDIA,
    LEAN_METHODOLOGY,
    MOBILE,
    FINANCE,
    BUSINESS,
    DOMAIN_EXPERTISE
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
