package net.sparkmuse.data.paging;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Size of a page for given type
 *
 * @author neteller
 * @created: Feb 19, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PagingSize {

  int value() default 20;

}
