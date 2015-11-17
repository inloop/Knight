package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link KnightActivity} is used for Activity subclasses that will be injected.
 *
 * @author Frantisek Gazo
 * @version 2015-09-22
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface KnightActivity {

    /**
     * <p>
     * List of classes that can be inside annotated activity.
     * </p>
     * <p>
     * <b> NOTE: </b>
     * Supported are only View and Fragment subclasses.
     * </p>
     */
    Class[] value() default {};

}
