package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ComponentFactoryBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public class ComponentFactoryBuilder extends BaseClassBuilder {

    public ComponentFactoryBuilder(ClassName componentName) throws ProcessorError {
        super(GCN.COMPONENT_FACTORY, componentName, GPN.KNIGHT, GPN.DI, GPN.FACTORIES);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

}
