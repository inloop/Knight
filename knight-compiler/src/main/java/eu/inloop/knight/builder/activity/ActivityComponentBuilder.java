package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.BaseComponentBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ActivityComponentBuilder extends BaseComponentBuilder {

    public ActivityComponentBuilder(ClassName activityName) throws ProcessorError {
        super(EClass.ActivityScope, GCN.ACTIVITY_COMPONENT, activityName);
    }

}
