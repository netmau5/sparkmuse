package net.sparkmuse.data.mapper;

import java.util.Set;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.base.Function;
import net.sparkmuse.data.entity.Entity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class ObjectMapper {

  Map<Class<? extends Entity>, EntityMetadata> classToMetadataMap;

  public ObjectMapper(FieldMapperFactory fieldMapperFactory, Class<? extends Entity>... entityClasses) {
    final ImmutableMap.Builder<Class<? extends Entity>, EntityMetadata> builder = ImmutableMap.builder();
    for (Class<? extends Entity> clazz: entityClasses) {
      builder.put(clazz, new EntityMetadata(clazz, fieldMapperFactory)); 
    }
    classToMetadataMap = builder.build();
  }

  public <T extends Entity<T>, M> Function<T, M> newEntityToModelFunction(final Class<M> modelClass) {
    final ObjectMapper mapper = this;
    return new Function<T, M>(){
      public M apply(T t) {
        return mapper.fromEntity(t).to(modelClass);
      }
    };
  }

  public <T extends Entity<T>, M> Function<M, T> newModelToEntityFunction(final Class<T> entityClass) {
    final ObjectMapper mapper = this;
    return new Function<M, T>(){
      public T apply(M m) {
        return mapper.fromModel(m).to(entityClass);
      }
    };
  }

  public <T extends Entity<T>> ModelCreator fromEntity(T source) {
    return null == source ? new NoOpModelCreator() : new ModelCreatorImpl<T>(source);
  }

  public <U> EntityCreator fromModel(U source) {
    return null == source ? new NoOpEntityCreator() : new EntityCreatorImpl<U>(source);
  }

  public interface ModelCreator {
    <M> M to(final Class<M> target);
  }

  private class NoOpModelCreator implements ModelCreator {
    public Object to(Class target) {
      return null;
    }
  }

  private class ModelCreatorImpl<T extends Entity<T>> implements ModelCreator {

    private final T source;

    public ModelCreatorImpl(T source) {
      this.source = source;
    }

    public <M> M to(final Class<M> target) {
      try {
        final M model = target.newInstance();
        
        final EntityMetadata metadata = classToMetadataMap.get(source.getClass());
        final ImmutableSet<EntityMetadata.EntityField> entityFields = metadata.getFields();
        for (EntityMetadata.EntityField entityField: entityFields) {
          entityField.setModelValue(model, source);
        }
        return model;
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
  }

  public interface EntityCreator {
     <T extends Entity<T>> T to(Class<T> target);
  }

  private class NoOpEntityCreator implements EntityCreator  {
    public <T extends Entity<T>> T to(Class<T> target) {
      return null;
    }
  }

  private class EntityCreatorImpl<U> implements EntityCreator {

    private final U source;

    public EntityCreatorImpl(U source) {
      this.source = source;
    }

    public <T extends Entity<T>> T to(final Class<T> target) {
      try {
        final T entity = target.newInstance();

        final EntityMetadata metadata = classToMetadataMap.get(entity.getClass());
        final ImmutableSet<EntityMetadata.EntityField> entityFields = metadata.getFields();
        for (EntityMetadata.EntityField entityField: entityFields) {
          entityField.setEntityValue(entity, source);
        }

        return entity;
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
  }

}
