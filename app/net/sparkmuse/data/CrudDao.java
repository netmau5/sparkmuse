package net.sparkmuse.data;

import com.google.code.twig.FindCommand;
import net.sparkmuse.data.entity.Entity;

import java.util.List;

/**
 * @author neteller
 * @created: Dec 26, 2010
 */
public interface CrudDao {
  <T extends Entity<T>> List<T> read(FindCommand.RootFindCommand<T> findCommand);

  <T extends Entity<T>> T store(T entity);

  <T extends Entity<T>> T load(Class<T> entityClass, Long id);

  <T > T load(Class<T> entityClass, String key);
}
