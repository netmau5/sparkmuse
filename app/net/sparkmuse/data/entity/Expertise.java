package net.sparkmuse.data.entity;

/**
 * @author neteller
* @created: Jan 10, 2011
*/
public enum Expertise {
  BIG_DATA("Big Data"),
  BUSINESS("Business"),
  DESIGN("Design"),
  DOMAIN_EXPERTISE("Domain Expertise"),
  ENGINEERING("Engineering"),
  FINANCE("Finance"),
  LEAN_METHODOLOGY("Lean Methodology"),
  MARKETING("Marketing"),
  MOBILE("Mobile"),
  PRODUCT_DEVELOPMENT("Product Development"),
  SALES("Sales"),
  SEO("SEO"),
  SOCIAL_MEDIA("Social Media");

  private final String display;

  Expertise(String display) {
    this.display = display;
  }

  public String toDisplay() {
    return display;
  }

  public String toCss() {
    return "expertise-" + this.toString().toLowerCase().replace("_", "-");
  }
}
