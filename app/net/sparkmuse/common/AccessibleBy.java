package net.sparkmuse.common;

import net.sparkmuse.data.util.AccessLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * @author neteller
 * @created: Apr 27, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AccessibleBy {

  AccessLevel value() default AccessLevel.USER;

}
