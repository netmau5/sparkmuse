package net.sparkmuse.data.twig;

import net.sparkmuse.data.entity.Entity;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import com.google.appengine.api.datastore.Key;

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

  public static <U extends Entity<U>> U store(U entity, ObjectDatastore datastore) {
    if (null == entity) return null;

    //set the key on the model object
    final Key key = datastore.store().instance(entity).now();
    entity.setId(key.getId());

    return entity;
  }

  public static <U extends Entity<U>> U update(U entity, ObjectDatastore datastore) {
    if (null == entity) return null;

    //set the key on the model object
    associate(entity, datastore);
    datastore.update(entity);

    return entity;
  }

  public static <U extends Entity<U>> U storeOrUpdate(U entity, ObjectDatastore datastore) {
    if (null == entity) return null;

    if (null == entity.getId()) {
      return store(entity, datastore);
    }
    else {
      return update(entity, datastore);
    }
  }
}
