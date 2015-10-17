package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.builder.BaseFactoryBuilder;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityComponentFactoryBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ActivityComponentFactoryBuilder extends BaseFactoryBuilder {

    public ActivityComponentFactoryBuilder(ClassName ActivityComponentBuilder) throws ProcessorError {
        super(ActivityComponentBuilder);
    }

}
