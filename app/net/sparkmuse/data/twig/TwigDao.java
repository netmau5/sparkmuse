package net.sparkmuse.data.twig;

import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.CrudDao;
import com.google.common.base.Function;
import com.google.appengine.api.datastore.Cursor;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand;
import org.apache.commons.lang.StringUtils;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Sep 19, 2010
 */
public class TwigDao implements CrudDao {

  protected final ObjectDatastore datastore;
  protected final DatastoreService helper;
  
  public TwigDao(DatastoreService service) {
    this.helper = service;
    this.datastore = service.getDatastore();
  }

  public <T extends Entity<T>> List<T> read(FindCommand.RootFindCommand<T> findCommand) {
    return helper.all(findCommand);
  }

  public <T extends Entity<T>> T store(T entity) {
    if (null == entity.getId()) {
      return helper.store(entity);
    }
    else {
      return helper.update(entity);
    }
  }

  public <T extends Entity<T>> T update(T entity) {
    return store(entity);
  }

  public <T extends Entity<T>> T load(Class<T> entityClass, Long id) {
    return helper.load(entityClass, id);
  }
}
