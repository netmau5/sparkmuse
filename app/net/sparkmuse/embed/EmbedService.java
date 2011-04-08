package net.sparkmuse.embed;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * @author neteller
 * @created: Apr 7, 2011
 */
public class EmbedService {

  @JsonProperty("name")
  private String name;
  @JsonProperty("regex")
  private List<String> regexes;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getRegexes() {
    return regexes;
  }

  public void setRegexes(List<String> regexes) {
    this.regexes = regexes;
  }
}
