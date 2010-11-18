package net.sparkmuse.data.mapper;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public interface FieldMapper {
  Class getEntityFieldType();
  Object toModelField(Class targetClass, Object object);
  Object toEntityField(Class targetClass, Object object);
}
