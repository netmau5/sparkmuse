package extensions;

import play.templates.JavaExtensions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.swing.text.DateFormatter;

import net.sparkmuse.common.DatePrinter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010d
 */
public class DateTimeExtensions extends JavaExtensions {

  public static String format(DateTime dateTime) {
    return DatePrinter.timeElapsedSince(dateTime);
  }

  public static String format(DateTime dateTime, String format) {
    return DateTimeFormat.forPattern(format).print(dateTime);
  }

}


