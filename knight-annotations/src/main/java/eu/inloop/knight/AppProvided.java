package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation @{@link AppProvided} is used for any constructor of any class that will be provided from Application scope.
 *
 * @author Frantisek Gazo
 * @version 2015-09-22
 */
@Target({CONSTRUCTOR, METHOD, TYPE})
@Retention(SOURCE)
public @interface AppProvided {

    /**
     * <p>
     * True if the same instance should be provided during the life of specified scope. (Like Singleton)
     * </p>
     * <p>
     * <b> NOTE: </b>
     * Default value is {@code false}.
     * </p>
     */
    boolean scoped() default false;

}
