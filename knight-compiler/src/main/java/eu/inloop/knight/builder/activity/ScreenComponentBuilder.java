package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.BaseComponentBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ScreenComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ScreenComponentBuilder extends BaseComponentBuilder {

    public ScreenComponentBuilder(ClassName activityName) throws ProcessorError {
        super(EClass.ScreenScope, GCN.SCREEN_COMPONENT, activityName);
    }

}
