package eu.inloop.knight.builder.module;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import dagger.Module;
import dagger.Provides;
import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.builder.BaseClassBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.builder.GPN;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link BaseModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public abstract class BaseModuleBuilder extends BaseClassBuilder {

    // TODO : Implement save state mechanism into PROVIDES methods only for extended IStateful

    private static final String METHOD_NAME_PROVIDES = "provides%s";

    protected final Map<String, Integer> mProvidesMethodNames = new HashMap<>();
    private Class<? extends Annotation> mScope;

    public BaseModuleBuilder(Class<? extends Annotation> scope, GCN genClassName, ClassName className) throws ProcessorError {
        super(genClassName, className, GPN.KNIGHT, GPN.DI, GPN.MODULES);
        if (scope != AppScope.class && scope != ScreenScope.class && scope != ActivityScope.class) {
            throw new IllegalStateException("Unsupported Scope class.");
        }
        mScope = scope;
        addScopeSpecificPart();
    }

    public BaseModuleBuilder(Class<? extends Annotation> scope, GCN genClassName) throws ProcessorError {
        this(scope, genClassName, null);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        getBuilder().addAnnotation(Module.class);
    }

    public void addProvidesConstructor(ExecutableElement e) throws ProcessorError {
        if (!e.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessorError(e, ErrorMsg.Provided_constructor_not_public);
        }

        ClassName className = ClassName.get((TypeElement) e.getEnclosingElement());

        MethodSpec.Builder method = prepareProvidesMethodBuilder(e, className.simpleName(), isScoped(e));
        addProvideStatement(method, e, "new $T", className);
        method.returns(className);

        getBuilder().addMethod(method.build());
    }

    public void addProvidesMethod(ExecutableElement e) throws ProcessorError {
        if (!e.getModifiers().containsAll(Arrays.asList(Modifier.PUBLIC, Modifier.STATIC))) {
            throw new ProcessorError(e, ErrorMsg.Provided_method_not_public_static);
        }

        ClassName className = ClassName.get((TypeElement) e.getEnclosingElement());
        TypeName returnTypeName = ClassName.get(e.getReturnType());

        MethodSpec.Builder method = prepareProvidesMethodBuilder(e, e.getSimpleName().toString(), isScoped(e));
        addProvideStatement(method, e, "$T.$N", className, e.getSimpleName().toString());
        method.returns(returnTypeName);

        getBuilder().addMethod(method.build());
    }

    protected MethodSpec.Builder prepareProvidesMethodBuilder(ExecutableElement e, String methodNamePart) {
        return prepareProvidesMethodBuilder(e, methodNamePart, false);
    }

    protected MethodSpec.Builder prepareProvidesMethodBuilder(ExecutableElement e, String methodNamePart, boolean scoped) {
        String methodName = formatProvidesName(methodNamePart);

        MethodSpec.Builder method = MethodSpec.methodBuilder(methodName)
                .addAnnotation(Provides.class)
                .addModifiers(Modifier.PUBLIC);

        if (scoped) {
            method.addAnnotation(mScope);
        }
        // add also Annotations (except @Override)
        for (AnnotationSpec a : getAnnotations(e)) {
            method.addAnnotation(a);
        }
        return method;
    }

    private String formatProvidesName(final String methodName) {
        String name = String.format(METHOD_NAME_PROVIDES, StringUtils.startUpperCase(methodName));
        // make sure method name is unique in generated Module
        Integer i = mProvidesMethodNames.get(name);
        if (i == null) {
            mProvidesMethodNames.put(name, 1);
        } else {
            name += ++i;
            mProvidesMethodNames.put(name, i);
        }
        return name;
    }

    protected void addProvideStatement(MethodSpec.Builder method, ExecutableElement e, String callFormat, Object... args) {
        addProvideCode(true, method, e, callFormat, args);
    }

    protected void addProvideCode(boolean asReturnStatement, MethodSpec.Builder method, ExecutableElement e, String callFormat, Object... args) {
        if (asReturnStatement) method.addCode("return ");

        method.addCode(String.format("%s(", callFormat), args);
        for (int i = 0; i < e.getParameters().size(); i++) {
            VariableElement p = e.getParameters().get(i);
            Set<Modifier> modifiers = p.getModifiers();

            method.addCode("$N", p.getSimpleName().toString());
            if (i < e.getParameters().size() - 1) {
                method.addCode(", ");
            }

            ParameterSpec.Builder param = ParameterSpec.builder(
                    ClassName.get(p.asType()),
                    p.getSimpleName().toString(),
                    modifiers.toArray(new Modifier[modifiers.size()])
            );
            // add also Annotations
            for (AnnotationSpec a : getAnnotations(p)) {
                param.addAnnotation(a);
            }
            method.addParameter(param.build());
        }
        method.addCode(")");
        if (asReturnStatement) method.addCode(";\n");
    }

    protected abstract void addScopeSpecificPart();

    protected abstract boolean isScoped(Element e);

}
