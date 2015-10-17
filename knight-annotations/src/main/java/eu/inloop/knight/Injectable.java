package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation @{@link Injectable} is used for any class inside of @{@link Scoped} Activity, that will be injected.
 *
 * @author Frantisek Gazo
 * @version 2015-09-22
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface Injectable {

    /**
     * <p>
     * List of Activities that can contain class annotated with this @{@link Injectable}.
     * </p>
     * <p>
     * <b> WARNING: </b>
     * Each of these Activities must be annotated with @{@link Scoped}.
     * </p>
     */
    Class<?>[] fromScoped(); // TODO : Class<? extends Activity>

}
