package eu.inloop.knight.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import dagger.Component;
import dagger.Subcomponent;
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

    protected List<ClassName> getmModules() {
        return mModules;
    }

    public void addModule(ClassName module) {
        mModules.add(module);
    }

    public void addInjectMethod(ClassName className) {
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_INJECT)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(className, ProcessorUtils.getParamName(className))
                        .build()
        );
    }

    public void addPlusMethod(ClassName component, ClassName... modules) {
        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_PLUS)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(component);
        for (ClassName module : modules) {
            method.addParameter(module, ProcessorUtils.getParamName(module));
        }
        getBuilder().addMethod(method.build());
    }

}
