package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.builder.BaseModuleBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ActivityModuleBuilder extends BaseModuleBuilder {

    public ActivityModuleBuilder(ClassName activityName) throws ProcessorError {
        super(GCN.ACTIVITY_MODULE, activityName);
    }

}
