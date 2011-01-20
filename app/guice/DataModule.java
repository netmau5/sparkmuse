package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.PropertyTranslator;
import com.google.code.twig.Path;
import com.google.code.twig.Property;
import com.google.code.twig.util.SimpleProperty;
import com.google.code.twig.util.generic.GenericTypeReflector;
import net.sparkmuse.data.*;
import net.sparkmuse.data.twig.*;
import net.sparkmuse.common.PlayCache;
import net.sparkmuse.common.Cache;

import play.modules.twig.PlayDatastore;

import java.util.Set;
import java.util.Collections;
import java.lang.reflect.Type;

import org.joda.time.DateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PostDao.class).to(TwigPostDao.class);
    bind(SparkDao.class).to(TwigSparkDao.class);
    bind(UserDao.class).to(TwigUserDao.class);
    bind(ObjectDatastore.class).toInstance(new DatastoreProvider().get());
    bind(Cache.class).to(PlayCache.class);
  }

  private static class DatastoreProvider implements Provider<ObjectDatastore> {

    public ObjectDatastore get() {
      final PlayDatastore.Builder b = PlayDatastore.builder();

      //DateTime
      b.addTranslator(DateTime.class, new PropertyTranslator(){

        public Object decode(Set<Property> properties, Path path, Type type) {
          if (properties.isEmpty()) return null;
          if (DateTime.class.isAssignableFrom(GenericTypeReflector.erase(type))) {
            long time = (Long) properties.iterator().next().getValue();
            return new DateTime(time);
          }
          else {
            return null;
          }
        }

        public Set<Property> encode(Object instance, Path path, boolean indexed) {
          if (null == instance) {
            return Collections.emptySet();
          }
          if (instance instanceof DateTime) {
            Property property = new SimpleProperty(path, ((DateTime) instance).getMillis(), indexed);
            return Collections.singleton(property);
          }
          else {
            return null;
          }
        }

      });

      return b.build();
    }

  }

}
