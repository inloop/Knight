package eu.inloop.knight.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;

/**
 * Class {@link ComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public class ComponentBuilder extends BaseClassBuilder {

    private static final String METHOD_NAME_INJECT = "inject";
    private static final String METHOD_NAME_PLUS = "plus";

    private final Class<? extends Annotation> mScope;
    private final List<ClassName> mModules = new ArrayList<>();
    private final List<ExtendedScreenModuleBuilder> mESMBuilders = new ArrayList<>();

    public ComponentBuilder(Class<? extends Annotation> scope, GCN genClassName, ClassName className) throws ProcessorError {
        super(false, genClassName, className, GPN.KNIGHT, GPN.DI, GPN.COMPONENTS);
        if (scope != AppScope.class && scope != ScreenScope.class && scope != ActivityScope.class) {
            throw new IllegalStateException("Unsupported Scope class.");
        }
        mScope = scope;
    }

    public ComponentBuilder(Class<? extends Annotation> scope, GCN genClassName) throws ProcessorError {
        this(scope, genClassName, null);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC);
    }

    @Override
    public void end() throws ProcessorError {
        super.end();

        // set to Scope Scope
        getBuilder().addAnnotation(mScope);

        // add Component Annotation
        StringBuilder modulesFormat = new StringBuilder();
        modulesFormat.append("{").append("\n");
        for (int i = 0; i < mModules.size(); i++) {
            modulesFormat.append("$T.class,").append("\n");
        }
        modulesFormat.append("}");

        getBuilder().addAnnotation(
                AnnotationSpec.builder((mScope == AppScope.class) ? Component.class : Subcomponent.class)
                        .addMember("modules", modulesFormat.toString(), mModules.toArray())
                        .build()
        );
    }

    protected List<ClassName> getModules() {
        return mModules;
    }

    public void addModule(ClassName module) {
        mModules.add(module);
    }

    public void addModule(TypeElement moduleElement) throws ProcessorError {
        if (moduleElement.getAnnotation(Module.class) == null) {
            throw new ProcessorError(moduleElement, ErrorMsg.Missing_Module_annotation);
        }
        // check empty constructor - all external modules has to have an empty constructor
        boolean hasEmptyConstructor = false;
        for (Element e : moduleElement.getEnclosedElements()) {
            if (e.getKind() == ElementKind.CONSTRUCTOR && ((ExecutableElement) e).getParameters().isEmpty()) {
                hasEmptyConstructor = true;
                break;
            }
        }
        if (!hasEmptyConstructor) {
            throw new ProcessorError(moduleElement, ErrorMsg.Screen_Module_without_empty_constructor);
        }
        // module class has to be public
        if (!moduleElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessorError(moduleElement, ErrorMsg.Module_not_public);
        }
        // module class cannot be abstract
        if (moduleElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessorError(moduleElement, ErrorMsg.Module_is_abstract);
        }

        if (mScope == ScreenScope.class) {
            // Screen Module cannot be final
            if (moduleElement.getModifiers().contains(Modifier.FINAL)) {
                throw new ProcessorError(moduleElement, ErrorMsg.Screen_Module_is_final);
            }

            ExtendedScreenModuleBuilder esm = new ExtendedScreenModuleBuilder(moduleElement);
            mESMBuilders.add(esm);
            addModule(esm.getClassName());
        } else {
            addModule(ClassName.get(moduleElement));
        }
    }

    public void addInjectMethod(ClassName className) {
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_INJECT)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(className, ProcessorUtils.getParamName(className))
                        .build()
        );
    }

    public void addPlusMethod(ComponentBuilder componentBuilder) {
        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_PLUS)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(componentBuilder.getClassName());
        for (ClassName module : componentBuilder.getModules()) {
            method.addParameter(module, ProcessorUtils.getParamName(module));
        }
        getBuilder().addMethod(method.build());
    }

    @Override
    public void build(Filer filer) throws ProcessorError, IOException {
        for (ExtendedScreenModuleBuilder e : mESMBuilders) {
            e.build(filer);
        }
        super.build(filer);
    }

    public Class<? extends Annotation> getScope() {
        return mScope;
    }
}
