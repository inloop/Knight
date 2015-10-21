package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation @{@link Scoped} is used only for Activity classes that will be injected.
 *
 * @author Frantisek Gazo
 * @version 2015-09-22
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface Scoped {

    With[] value() default {};

}
