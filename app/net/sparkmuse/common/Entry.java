package net.sparkmuse.common;

/**
 * @author neteller
 * @created: Jan 8, 2011
 */
public class Entry<T, U> {

  private final T t;
  private final U u;

  public Entry(T t, U u) {
    this.t = t;
    this.u = u;
  }

  public T getKey() {
    return t;
  }

  public U getValue() {
    return u;
  }

}
