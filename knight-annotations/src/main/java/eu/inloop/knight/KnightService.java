package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation @{@link KnightService} is used for Service subclasses that will be injected from Application Scope.
 *
 * @author FrantisekGazo
 * @version 2015-11-10
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface KnightService {
}

