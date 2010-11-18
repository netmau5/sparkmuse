package net.sparkmuse.data.mapper;

import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;

import org.joda.time.DateTime;
import play.Logger;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class FieldMapperFactory {

  private final Map<Class, FieldMapper> entityFieldTypeClassToMapper;

  @Inject
  public FieldMapperFactory(List<FieldMapper> mappers) {
    this.entityFieldTypeClassToMapper = Maps.uniqueIndex(mappers, new Function<FieldMapper, Class>(){
      public Class apply(FieldMapper fieldMapper) {
        return fieldMapper.getEntityFieldType();
      }
    });
  }

  public FieldMapper getMapperFor(final Field entityField){
    Class type = entityField.getType();
    while(null != type) {
      final FieldMapper mapper = this.entityFieldTypeClassToMapper.get(type);
      if (null != mapper) return mapper;
      type = type.getSuperclass();
    }
    throw new IllegalArgumentException("No mapper defined for type [" + entityField.getType() + "]");
  }

  public static class StandardMapper implements FieldMapper {
    public Class getEntityFieldType() {
      return Object.class;
    }

    public Object toModelField(Class targetClass, Object object) {
      return object;
    }
    public Object toEntityField(Class targetClass, Object object) {
      return object;
    }
  }

  public static class EnumMapper implements FieldMapper {
    public Class getEntityFieldType() {
      return Enum.class;
    }

    public Object toModelField(Class targetClass, Object object) {
      if (null == object) return null;

      Preconditions.checkArgument(targetClass == String.class);
      return object.toString();
    }
    public Object toEntityField(Class targetClass, Object object) {
      if (null == object) return null;

      Preconditions.checkArgument(Enum.class.isAssignableFrom(targetClass));
      return Enum.valueOf(targetClass, object.toString());
    }
  }

  public static class DateTimeMapper implements FieldMapper {
    public Class getEntityFieldType() {
      return DateTime.class;
    }

    public Object toModelField(Class targetClass, Object object) {
      if (null == object) return null;

      Preconditions.checkArgument(targetClass == Long.class);
      Preconditions.checkArgument(object instanceof DateTime);
      return ((DateTime) object).getMillis();
    }
    public Object toEntityField(Class targetClass, Object object) {
      if (null == object) return null;

      Preconditions.checkArgument(targetClass == DateTime.class);
      Preconditions.checkArgument(object instanceof Long);
      return new DateTime((Long) object);
    }
  }

  public static class ClassMapper implements FieldMapper {
    public Class getEntityFieldType() {
      return Class.class;
    }

    public Object toModelField(Class targetClass, Object object) {
      if (null == object) return null;

      Preconditions.checkArgument(targetClass == String.class);
      Preconditions.checkArgument(object instanceof Class);
      return ((Class) object).getName();
    }
    public Object toEntityField(Class targetClass, Object object) {
      if (null == object) return null;

      Preconditions.checkArgument(targetClass == Class.class);
      Preconditions.checkArgument(object instanceof String);
      try {
        return Class.forName(object.toString());
      } catch (ClassNotFoundException e) {
        Logger.warn("Field mapper could not find class named [" + object.toString() + "]");
        return null;
      }
    }
  }
}
