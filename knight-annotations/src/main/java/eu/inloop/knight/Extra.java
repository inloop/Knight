package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link Extra}
 *
 * @author FrantisekGazo
 * @version 2015-11-17
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Extra {

    /**
     * <p>
     * Name for annotated extra. If not specified, field name will be used.
     * </p>
     */
    String value() default "";

}
