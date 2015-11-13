package eu.inloop.knight;

/**
 * Annotation @{@link With} is used to represent an Extra for started Activity.
 *
 * @author FrantisekGazo
 * @version 2015-10-21
 */
public @interface With {

    String name();

    Class type();

    String withNamed() default "";

    boolean withNullable() default false;

}
