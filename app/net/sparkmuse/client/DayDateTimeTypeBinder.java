package net.sparkmuse.client;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import play.data.binding.TypeBinder;
import play.data.binding.Global;

import java.lang.annotation.Annotation;

/**
 * @author neteller
 * @created: Mar 24, 2011
 */
@Global
public class DayDateTimeTypeBinder implements TypeBinder<DateTime> {

  public Object bind(String name, Annotation[] annotations, String value, Class actualClass) throws Exception {
    return DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(value);
  }
}
