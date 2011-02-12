package net.sparkmuse.activity;

/**
 * Indicates an entity is tracked for user notification purposes.
 *
 * @author neteller
 * @created: Feb 12, 2011
 */
public interface Notifiable {

  /**
   * If this entity has been processed for notifications
   *
   * @return
   */
  boolean isNotified();

  void setNotified(boolean notified);

}
