package eu.inloop.knight.generator;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ApplicationComponentGenerator}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ApplicationComponentGenerator extends BaseClassGenerator {

    public ApplicationComponentGenerator() throws ProcessorError {
        super(GCN.APPLICATION_COMPONENT, GPN.KNIGHT, GPN.DI, GPN.COMPONENTS);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public void end() throws ProcessorError {

    }

}
