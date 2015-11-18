package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link AppProvided} is used for any constructor of any class that will be provided from Application scope.
 *
 * @author Frantisek Gazo
 * @version 2015-09-22
 */
@Target({CONSTRUCTOR, METHOD, TYPE})
@Retention(RUNTIME)
public @interface AppProvided {
}
