package functional;

import com.google.inject.Injector;
import com.google.inject.Guice;
import com.google.code.twig.ObjectDatastore;
import models.UserModel;
import models.PostModel;
import models.SparkModel;
import guice.DataModule;
import guice.TaskModule;
import guice.LocalTwitterModule;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;
import play.vfs.VirtualFile;
import play.Play;
import play.PlayPlugin;
import play.exceptions.YAMLException;
import play.data.binding.Binder;
import play.db.Model;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class FunctionalTestUtils {

  private static Injector TEST_INJECTOR;

  public static <T> T getInstance(Class<T> clazz) {
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

//  static Pattern keyPattern = Pattern.compile("([^(]+)\\(([^)]+)\\)");
//
//  public static void loadTestData(String name) {
//    VirtualFile yamlFile = null;
//    try {
//      for (VirtualFile vf : Play.javaPath) {
//        yamlFile = vf.child(name);
//        if (yamlFile != null && yamlFile.exists()) {
//          break;
//        }
//      }
//      InputStream is = Play.classloader.getResourceAsStream(name);
//      if (is == null) {
//        throw new RuntimeException("Cannot load fixture " + name + ", the file was not found");
//      }
//      Yaml yaml = new Yaml();
//      Object o = yaml.load(is);
//      if (o instanceof LinkedHashMap<?, ?>) {
//        @SuppressWarnings("unchecked") LinkedHashMap<Object, Map<?, ?>> objects = (LinkedHashMap<Object, Map<?, ?>>) o;
//        Map<String, Object> idCache = new HashMap<String, Object>();
//        for (Object key: objects.keySet()) {
//          Matcher matcher = keyPattern.matcher(key.toString().trim());
//          if (matcher.matches()) {
//            String type = matcher.group(1);
//            String id = matcher.group(2);
//            if (!type.startsWith("models.")) {
//              type = "models." + type;
//            }
//            if (idCache.containsKey(type + "-" + id)) {
//              throw new RuntimeException("Cannot load fixture " + name + ", duplicate id '" + id + "' for type " + type);
//            }
//            Map<String, String[]> params = new HashMap<String, String[]>();
//            if (objects.get(key) == null) {
//              objects.put(key, new HashMap<Object, Object>());
//            }
//          }
//        }
//      }
////    } catch (ClassNotFoundException e) {
////      throw new RuntimeException("Class " + e.getMessage() + " was not found", e);
//    } catch (ScannerException e) {
//      throw new YAMLException(e, yamlFile);
//    } catch (Throwable e) {
//      throw new RuntimeException("Cannot load fixture " + name + ": " + e.getMessage(), e);
//    }
//  }

}
