package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link As}
 *
 * @author FrantisekGazo
 * @version 2015-11-17
 */
@Target({CONSTRUCTOR, METHOD})
@Retention(RUNTIME)
public @interface As {

    Class value();

}
