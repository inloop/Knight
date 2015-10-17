package eu.inloop.knight.generator;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityModuleGenerator}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ActivityModuleGenerator extends BaseClassGenerator {

    public ActivityModuleGenerator(ClassName activityName) throws ProcessorError {
        super(GCN.ACTIVITY_MODULE, activityName, GPN.KNIGHT, GPN.DI, GPN.MODULES);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public void end() throws ProcessorError {

    }

}
