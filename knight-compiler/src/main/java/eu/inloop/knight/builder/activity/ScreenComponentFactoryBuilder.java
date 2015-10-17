package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.builder.BaseFactoryBuilder;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ScreenComponentFactoryBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ScreenComponentFactoryBuilder extends BaseFactoryBuilder {

    public ScreenComponentFactoryBuilder(ClassName componentName) throws ProcessorError {
        super(componentName);
    }

}
