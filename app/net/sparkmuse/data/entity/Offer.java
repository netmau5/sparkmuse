package net.sparkmuse.data.entity;

import play.data.validation.Required;
import play.data.validation.CheckWith;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;
import net.sparkmuse.client.NoScriptCheck;

/**
 * @author neteller
 * @created: Jan 6, 2011
 */
public class Offer extends Entity<Offer> {

  @Required
  private Expertise expertise;

  @Type(Text.class)
  private String message;

  @Type(Text.class)
  @CheckWith(value=NoScriptCheck.class, message="validation.noscript")
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
