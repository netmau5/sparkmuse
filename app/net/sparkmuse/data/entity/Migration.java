package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Id;

/**
 * @author neteller
 * @created: Jan 17, 2011
 */
public class Migration {

  @Id
  private String id;
  private State state;

  public enum State {
    STARTED,
    COMPLETED
  }

  public Migration() {
  }

  public Migration(String id, State state) {
    this.id = id;
    this.state = state;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

}
