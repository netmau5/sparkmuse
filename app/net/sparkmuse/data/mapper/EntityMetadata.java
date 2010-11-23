package net.sparkmuse.data.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sparkmuse.data.entity.Entity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class EntityMetadata<T extends Entity> {

  Map<String, Method> gettersByPropertyName;
  Map<String, Method> settersByPropertyName;
  Map<String, FieldMapper> fieldMapperByPropertyName;
  Iterable<Field> fields;

  public EntityMetadata(Class<T> clazz, FieldMapperFactory fieldMapperFactory) {
    final ImmutableMap.Builder<String, Method> getterBuilder = ImmutableMap.builder();
    final ImmutableMap.Builder<String, Method> setterBuilder = ImmutableMap.builder();
    final ImmutableMap.Builder<String, FieldMapper> mapperBuilder = ImmutableMap.builder();

    fields = fieldsFor(clazz);
    for(final Field field: fields) {
      final String propertyName = propertyNameOf(field);
      getterBuilder.put(propertyName, getterFor(field, clazz));
      setterBuilder.put(propertyName, setterFor(field, clazz));
      mapperBuilder.put(propertyName, fieldMapperFactory.getMapperFor(field));
    }
    gettersByPropertyName = getterBuilder.build();
    settersByPropertyName = setterBuilder.build();
    fieldMapperByPropertyName = mapperBuilder.build();
  }

  public Set<String> getPropertyNames(){
    return gettersByPropertyName.keySet();
  }

  public Iterable<Field> getFields() {
    return fields;
  }

  public Method getterFor(final String propertyName){
    return gettersByPropertyName.get(propertyName);
  }

  public Method setterFor(final String propertyName){
    return settersByPropertyName.get(propertyName);
  }

  public FieldMapper mapperFor(final String propertyName) {
    return fieldMapperByPropertyName.get(propertyName);
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

  private String propertyNameOf(Field field) {
    final Property annotation = field.getAnnotation(Property.class);
    if (null == annotation) return field.getName();
    else return annotation.value();
  }

  private Method getterFor(Field field, Class clazz) {
    String name = field.getName();
    name = name.substring(0, 1).toUpperCase() + name.substring(1);
    if (boolean.class == field.getType() || Boolean.class == field.getType()) {
      name = "is" + name;
    }
    else {
      name = "get" + name;
    }
    try {
      return clazz.getMethod(name);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No getter defined for field [" + field.getName() + "]");
    }
  }

  private Method setterFor(Field field, Class clazz) {
    String name = field.getName();
    name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    try {
      return clazz.getMethod(name, field.getType());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No setter defined for field [" + field.getName() + "]");
    }
  }
}
