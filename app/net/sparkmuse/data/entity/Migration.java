package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Id;
import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Jan 17, 2011
 */
public class Migration {

  @Id
  private String id;
  private State state;

  private DateTime started;
  private DateTime ended;

  public enum State {
    STARTED,
    COMPLETED
  }

  public Migration() {
    this.started = new DateTime();
  }

  public Migration(String id, State state) {
    this();
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

  public DateTime getStarted() {
    return started;
  }

  public void setStarted(DateTime started) {
    this.started = started;
  }

  public DateTime getEnded() {
    return ended;
  }

  public void setEnded(DateTime ended) {
    this.ended = ended;
  }
}
