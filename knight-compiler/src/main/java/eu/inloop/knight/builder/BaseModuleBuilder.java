package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link BaseModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public abstract class BaseModuleBuilder extends BaseClassBuilder {

    public BaseModuleBuilder(GCN genClassName, ClassName className) throws ProcessorError {
        super(genClassName, className, GPN.KNIGHT, GPN.DI, GPN.MODULES);
    }

    public BaseModuleBuilder(GCN genClassName) throws ProcessorError {
        super(genClassName, GPN.KNIGHT, GPN.DI, GPN.MODULES);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        getBuilder().addAnnotation(EClass.Module.getName());
    }

}
