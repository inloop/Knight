package eu.inloop.knight.builder.component;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;

import dagger.Subcomponent;
import eu.inloop.knight.core.IActivityComponent;
import eu.inloop.knight.core.IComponent;
import eu.inloop.knight.name.GCN;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class ActivityComponentBuilder extends BaseComponentBuilder {

    public ActivityComponentBuilder(ClassName className) throws ProcessorError {
        super(ActivityScope.class, GCN.ACTIVITY_COMPONENT, className);
    }

    @Override
    protected Class<? extends Annotation> getComponentAnnotation() {
        return Subcomponent.class;
    }

    @Override
    protected Class<? extends IComponent> getComponentInterface() {
        return IActivityComponent.class;
    }
}
