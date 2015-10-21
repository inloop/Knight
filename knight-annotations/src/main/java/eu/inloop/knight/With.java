package eu.inloop.knight;

/**
 * Class {@link With}
 *
 * @author FrantisekGazo
 * @version 2015-10-21
 */
public @interface With {
    String name();

    Class type();

    String withNamed() default "";
}
