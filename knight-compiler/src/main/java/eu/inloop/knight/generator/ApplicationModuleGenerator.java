package eu.inloop.knight.generator;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ApplicationModuleGenerator}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ApplicationModuleGenerator extends BaseClassGenerator {

    public ApplicationModuleGenerator() throws ProcessorError {
        super(GCN.APPLICATION_MODULE, GPN.KNIGHT, GPN.DI, GPN.MODULES);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public void end() throws ProcessorError {

    }

}
