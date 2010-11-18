package net.sparkmuse.data.twig;

import com.vercer.engine.persist.FindCommand;
import com.vercer.engine.persist.ObjectDatastore;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Cursor;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterators;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Functions;

import java.util.List;
import java.lang.reflect.Field;

import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.common.TimedTransformer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 6, 2010
 */
public class DatastoreHelper {

  private final ObjectMapper map;
  private final ObjectDatastore datastore;

  public DatastoreHelper(ObjectDatastore datastore, ObjectMapper map) {
    this.map = map;
    this.datastore = datastore;
  }

  public final <T> T only(FindCommand.RootFindCommand<T> findCommand) {
    final QueryResultIterator<T> resultsIterator = findCommand.returnResultsNow();
    if (resultsIterator.hasNext()) {
      final T toReturn = resultsIterator.next();
      Preconditions.checkState(!resultsIterator.hasNext(), "Only one result requested but more than one returned.");
      return toReturn;
    }
    else return null;
  }

  public final <T, U extends Entity<U>> U only(Class<U> entityClass, FindCommand.RootFindCommand<T> findCommand) {
    return map.fromModel(only(findCommand)).to(entityClass);
  }

  public final <U extends Entity<U>> U load(Class<U> entityClass, Long id) {
    return map.fromModel(datastore.load(Entity.modelClassFor(entityClass), id)).to(entityClass); 
  }

  public final <T, U extends Entity<U>> List<U> all(final Class<U> entityClass, FindCommand.RootFindCommand<T> findCommand) {
    final QueryResultIterator<T> resultIterator = findCommand.returnResultsNow();
    return Lists.newArrayList(Iterators.transform(resultIterator, new Function<T, U>(){
      public U apply(T model) {
        return map.fromModel(model).to(entityClass);
      }
    }));
  }

  public final <T, U extends Entity<U>> U store(U entity) {
    if (null == entity) return null;
    final T model = map.fromEntity(entity).to((Class<T>) entity.getModelClass());

    //this should set the key on the model object automatically
    final Key key = datastore.store().instance(model).returnKeyNow();
    entity.setId(key.getId());

    return entity;
  }

  /**
   * Executes a timed job that will process a transformation over a given entity.  The transformation
   * logic is limited at approximately 15 seconds.  If the job doesn't complete in the allowed time, a
   * serialized cursor will be returned so that the job can be restarted at the last position.
   *
   * @param entityClass
   * @param transformer
   * @param find
   * @return serialized cursor
   */
  public <T, U extends Entity<U>> String execute(final Class<U> entityClass,
                                                 final Function<U, U> transformer,
                                                 final FindCommand.RootFindCommand<T> find) {
    final QueryResultIterator<T> iterator = find.returnResultsNow();

    final Function<T, U> modelToEntity = map.newModelToEntityFunction(entityClass);
    final Function<U, T> entityToModel = map.newEntityToModelFunction(Entity.modelClassFor(entityClass));

    final Function<T, T> transformFunction = new Function<T, T>() {
      public T apply(T t) {
        final U entity = modelToEntity.apply(t);
        final U transformedEntity = transformer.apply(entity);
        final T model = entityToModel.apply(transformedEntity);
        datastore.update(model);
        return model;
      }
    };

    new TimedTransformer(transformFunction).transform(iterator);

    if (iterator.hasNext()) return iterator.getCursor().toWebSafeString();
    else return null;
  }

}
