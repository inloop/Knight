package eu.inloop.knight.builder.app;

import eu.inloop.knight.builder.BaseModuleBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ApplicationModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ApplicationModuleBuilder extends BaseModuleBuilder {

    public ApplicationModuleBuilder() throws ProcessorError {
        super(GCN.APPLICATION_MODULE);
    }

}
