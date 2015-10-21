package eu.inloop.knight.builder.component;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
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
        boolean res = super.checkModuleElement(moduleElement);
        List<ExecutableElement> scopedMethods = getScreenScopedMethods(moduleElement);
        if (scopedMethods.isEmpty()) {
            return res;
        } else {
            // Screen Module cannot be final if contains ScreenScoped
            if (moduleElement.getModifiers().contains(Modifier.FINAL)) {
                throw new ProcessorError(moduleElement, ErrorMsg.Screen_Module_is_final);
            }

            ExtendedScreenModuleBuilder esm = new ExtendedScreenModuleBuilder(moduleElement, scopedMethods);
            addExtendedModule(esm);
            addModule(esm.getClassName());
            return false;
        }
    }

    private List<ExecutableElement> getScreenScopedMethods(TypeElement moduleElement) {
        List<ExecutableElement> scopedMethods = new ArrayList<>();
        for (Element e : moduleElement.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD && e.getAnnotation(ScreenScope.class) != null) {
                scopedMethods.add((ExecutableElement) e);
            }
        }
        return scopedMethods;
    }


}
