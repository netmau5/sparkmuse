package net.sparkmuse.data.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.lang.reflect.Field;

import net.sparkmuse.data.entity.Entity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class EntityMetadata<T extends Entity<T>> {

  private ImmutableSet<EntityField<T>> fields;
  private FieldMapperFactory fieldMapperFactory;

  public EntityMetadata(Class<T> clazz, FieldMapperFactory fieldMapperFactory) {
    this.fieldMapperFactory = fieldMapperFactory;

    final ImmutableSet.Builder<EntityField<T>> fieldsBuilder = ImmutableSet.builder();
    for(final Field field: fieldsFor(clazz)) {
      fieldsBuilder.add(new EntityField<T>(field));
    }
    fields = fieldsBuilder.build();
  }

  public ImmutableSet<EntityField<T>> getFields() {
    return fields;
  }

  private <T> List<Field> fieldsFor(Class<T> clazz) {
    final List<Field> toReturn = Lists.newArrayList();
    final Field[] fields = clazz.getDeclaredFields();
    for (Field field: fields) {
      if (null != field.getAnnotation(Property.class)) {
        field.setAccessible(true);
        toReturn.add(field);
      }
    }

    if (null != clazz.getSuperclass()) {
      toReturn.addAll(fieldsFor(clazz.getSuperclass()));
    }

    return toReturn;
  }

  private static String propertyNameOf(Field field) {
    final Property annotation = field.getAnnotation(Property.class);
    if (null == annotation) return field.getName();
    else return annotation.value();
  }

  public class EntityField<T extends Entity> {

    private final Field field;
    private final FieldMapper fieldMapper;
    private String property;

    public EntityField(Field field) {
      this.field = field;
      this.property = propertyNameOf(field);
      this.fieldMapper = fieldMapperFactory.getMapperFor(field);
    }

    public void setModelValue(Object modelObject, T entityObject) {
      try {
        final Field modelField = modelObject.getClass().getField(this.property);
        final Object value = fieldMapper.toModelField(modelField.getType(), field.get(entityObject));
        modelField.set(modelObject, value);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }

    public void setEntityValue(T entityObject, Object modelObject) {
      try {
        final Field modelField = modelObject.getClass().getField(this.property);
        final Object value = fieldMapper.toEntityField(field.getType(), modelField.get(modelObject));
        field.set(entityObject, value);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
  }
}
