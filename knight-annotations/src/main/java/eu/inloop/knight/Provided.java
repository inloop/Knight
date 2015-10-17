package eu.inloop.knight;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation @{@link Provided} is used for any constructor of any class that will be provided from Activity scope.
 *
 * @author Frantisek Gazo
 * @version 2015-09-22
 */
@Target({CONSTRUCTOR, TYPE}) // TODO : allow also 'TYPE' -> but must have also dagger.@Module
@Retention(SOURCE)
public @interface Provided {



    // TODO : divide this into 3 annotations:
    // @AppProvided
    // @ScreenProvided
    // @ActivityProvided




    /**
     * <p>
     * Scope in which instance will be provided.
     * </p>
     * <p>
     * <b> NOTE: </b>
     * Default value is {@link Scope#ACTIVITY}.
     * </p>
     */
    Scope inside() default Scope.ACTIVITY;

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

    /**
     * <p>
     * List of Activities.
     * </p>
     * <p>
     * <b> NOTE: </b>
     * If scope is {@link Scope#APPLICATION}, then this list should be empty. Otherwise warning will be shown.
     * </p>
     * <p>
     * <b> WARNING: </b>
     * Each of these Activities must be annotated with @{@link Scoped}.
     * </p>
     */
    Class<?>[] forScoped() default {}; // TODO : Class<? extends Activity>

    /**
     * <p>
     * Custom name for provide-method. Will be appended with 'provides'.
     * </p>
     * <p>
     * <b> NOTE: </b>
     * The name of provide-method is by default derived from class name.
     * (e.g. class Example -> 'providesExample()')
     * </p>
     * <p>
     * <b> NOTE 2: </b>
     * This is useful if you have annotated 2 classes with the same name and same constructor parameters from different packages.
     * </p>
     */
    String provideName() default "";

}
