package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Id;
import org.joda.time.DateTime;

/**
 * @author neteller
 * @created: Jan 17, 2011
 */
public class Migration {

  @Id
  private Long id;

  private String taskName;
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

  public Migration(String taskName, State state) {
    this();
    this.taskName = taskName;
    this.state = state;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
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
