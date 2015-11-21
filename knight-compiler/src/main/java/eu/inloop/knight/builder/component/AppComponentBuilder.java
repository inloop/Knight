package eu.inloop.knight.builder.component;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;

import dagger.Component;
import eu.inloop.knight.core.IAppComponent;
import eu.inloop.knight.core.IComponent;
import eu.inloop.knight.name.GCN;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class AppComponentBuilder extends BaseComponentBuilder {

    public AppComponentBuilder(ClassName appClassName) throws ProcessorError {
        super(AppScope.class, GCN.APPLICATION_COMPONENT, appClassName);
    }

    @Override
    protected Class<? extends Annotation> getComponentAnnotation() {
        return Component.class;
    }

    @Override
    protected Class<? extends IComponent> getComponentInterface() {
        return IAppComponent.class;
    }
}
