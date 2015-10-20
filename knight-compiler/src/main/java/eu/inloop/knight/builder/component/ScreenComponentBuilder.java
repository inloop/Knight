package eu.inloop.knight.builder.component;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import dagger.Subcomponent;
import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.builder.module.ExtendedScreenModuleBuilder;
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

    @Override
    protected boolean checkModuleElement(TypeElement moduleElement) throws ProcessorError {
        super.checkModuleElement(moduleElement);
        // Screen Module cannot be final
        if (moduleElement.getModifiers().contains(Modifier.FINAL)) {
            throw new ProcessorError(moduleElement, ErrorMsg.Screen_Module_is_final);
        }

        ExtendedScreenModuleBuilder esm = new ExtendedScreenModuleBuilder(moduleElement);
        addExtendedModule(esm);
        addModule(esm.getClassName());
        return false;
    }
}
