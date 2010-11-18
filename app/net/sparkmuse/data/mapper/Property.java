package net.sparkmuse.data.mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Property {

  String value();
  
}
