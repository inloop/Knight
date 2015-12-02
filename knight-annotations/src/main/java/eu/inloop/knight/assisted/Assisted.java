package eu.inloop.knight.assisted;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link Assisted}
 *
 * @author FrantisekGazo
 * @version 2015-11-17
 */
@Target({PARAMETER})
@Retention(RUNTIME)
public @interface Assisted {

    boolean id() default false;

}
