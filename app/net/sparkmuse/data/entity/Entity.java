package net.sparkmuse.data.entity;

import net.sparkmuse.data.Cacheable;
import net.sparkmuse.data.mapper.Model;
import net.sparkmuse.data.mapper.Property;
import net.sparkmuse.common.CacheKey;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import com.google.common.base.Preconditions;

/**
 * Layer supertype.  All entities must be standard Java POJOs so that they
 * may be serialized to JSON.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public abstract class Entity<T> implements Cacheable<T>, Serializable {

  @Property("id")
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonIgnore
  public CacheKey<T> getKey() {
    return new CacheKey(this.getClass(), id);
  }

  @JsonIgnore
  public T getInstance() {
    return (T) this;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Entity && this.getKey().equals(((Entity) o).getKey());
  }

  @JsonIgnore
  public Class<T> getModelClass() {
    return modelClassFor(this.getClass());
  }

  public static <T extends Entity<T>> Class modelClassFor(Class<T> entityClass) {
    final Model annotation = entityClass.getAnnotation(Model.class);
    Preconditions.checkNotNull(annotation, "Entity class missing Model annotation.");
    return annotation.value();
  }
}
