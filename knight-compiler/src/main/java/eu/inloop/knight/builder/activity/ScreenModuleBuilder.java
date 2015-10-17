package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.builder.BaseModuleBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ScreenModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ScreenModuleBuilder extends BaseModuleBuilder {

    public ScreenModuleBuilder(ClassName className) throws ProcessorError {
        super(GCN.SCREEN_MODULE, className);
    }

}
