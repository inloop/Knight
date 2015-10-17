package eu.inloop.knight.builder.app;

import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.BaseComponentBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ApplicationComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ApplicationComponentBuilder extends BaseComponentBuilder {

    public ApplicationComponentBuilder() throws ProcessorError {
        super(EClass.AppScope, GCN.APPLICATION_COMPONENT);
    }

}
