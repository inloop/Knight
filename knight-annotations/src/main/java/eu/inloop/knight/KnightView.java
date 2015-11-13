package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link KnightView} is used for View and Fragment subclasses that will be injected.
 *
 * @author FrantisekGazo
 * @version 2015-11-10
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface KnightView {

    /**
     * <p>
     * List of Activity classes that can contain class annotated with this annotation.
     * </p>
     * <p>
     * <b> NOTE: </b>
     * If left empty, class will be injectable only from Application Scope.
     * </p>
     * <p>
     * <b> WARNING: </b>
     * Each of these Activity classes must be annotated with @{@link KnightActivity}.
     * </p>
     */
    Class<?>[] in();

}

