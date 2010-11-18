package functional;

import com.google.inject.Injector;
import com.google.inject.Guice;
import com.vercer.engine.persist.ObjectDatastore;
import models.UserModel;
import models.PostModel;
import models.SparkModel;
import guice.DataModule;
import guice.TaskModule;
import guice.LocalTwitterModule;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class FunctionalTestUtils {

  private static Injector TEST_INJECTOR;

  public static <T> T getInstance(Class<T> clazz){
    if (null == TEST_INJECTOR) {
      TEST_INJECTOR = Guice.createInjector(new DataModule(), new TaskModule(), new LocalTwitterModule());
    }
    return TEST_INJECTOR.getInstance(clazz);
  }

  public static void deleteAll() {
    final ObjectDatastore datastore = TEST_INJECTOR.getInstance(ObjectDatastore.class);
    datastore.deleteAll(UserModel.class);
    datastore.deleteAll(PostModel.class);
    datastore.deleteAll(SparkModel.class);
  }

}
