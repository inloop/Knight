package eu.inloop.knight.generator;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityComponentGenerator}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ActivityComponentGenerator extends BaseClassGenerator {

    public ActivityComponentGenerator(ClassName activityName) throws ProcessorError {
        super(GCN.ACTIVITY_COMPONENT, activityName, GPN.KNIGHT, GPN.DI, GPN.COMPONENTS);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public void end() throws ProcessorError {

    }

}
