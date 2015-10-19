package eu.inloop.knight.builder.component;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;

import dagger.Subcomponent;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.core.IComponent;
import eu.inloop.knight.core.IScreenComponent;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ScreenComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class ScreenComponentBuilder extends BaseComponentBuilder {

    public ScreenComponentBuilder(ClassName className) throws ProcessorError {
        super(ScreenScope.class, GCN.SCREEN_COMPONENT, className);
    }

    @Override
    protected Class<? extends Annotation> getComponentAnnotation() {
        return Subcomponent.class;
    }

    @Override
    protected Class<? extends IComponent> getComponentInterface() {
        return IScreenComponent.class;
    }

}
