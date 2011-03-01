package net.sparkmuse.data.twig;

import net.sparkmuse.data.entity.Entity;
import com.google.code.twig.ObjectDatastore;

/**
 * @author neteller
 * @created: Jan 17, 2011
 */
public class DatastoreUtils {

  public static <U> U associate(U entity, ObjectDatastore datastore) {
    if (null == entity) return entity;

    //this should set the key on the model object automatically
    if (datastore.associatedKey(entity) == null) datastore.associate(entity);
    return entity;
  }
  
}
