package eu.inloop.knight.assisted;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link Factory}
 *
 * @author FrantisekGazo
 * @version 2015-11-17
 */
@Target({METHOD, CONSTRUCTOR})
@Retention(RUNTIME)
public @interface Factory {
}
