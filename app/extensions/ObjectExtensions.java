package extensions;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import play.templates.JavaExtensions;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 26, 2010
 */
public class ObjectExtensions extends JavaExtensions {

  public static String toWords(Object object) {
    final String s = object.toString().replaceAll("_", " ");
    return StringUtils.capitalize(s.toLowerCase());
  }

  public static String toJson(Object object) {
    try {
      return new ObjectMapper().configure(SerializationConfig.Feature.INDENT_OUTPUT, true)
          .writeValueAsString(object);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

}


