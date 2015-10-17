package eu.inloop.knight.builder.app;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.builder.BaseFactoryBuilder;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ApplicationComponentFactoryBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ApplicationComponentFactoryBuilder extends BaseFactoryBuilder {

    public ApplicationComponentFactoryBuilder(ClassName componentName) throws ProcessorError {
        super(componentName);
    }

}
