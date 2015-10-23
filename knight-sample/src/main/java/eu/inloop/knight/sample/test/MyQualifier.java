package eu.inloop.knight.sample.test;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Interface {@link MyQualifier}
 *
 * @author FrantisekGazo
 * @version 2015-10-23
 */
@Qualifier
@Retention(RUNTIME)
public @interface MyQualifier {
}

