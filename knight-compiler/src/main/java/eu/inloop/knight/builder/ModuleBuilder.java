package eu.inloop.knight.builder;

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

import javax.inject.Named;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import dagger.Module;
import dagger.Provides;
import eu.inloop.knight.ActivityProvided;
import eu.inloop.knight.AppProvided;
import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link ModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public class ModuleBuilder extends BaseClassBuilder {

    // TODO : Implement save state mechanism into PROVIDES methods

    private static class Attr {
        boolean scoped;
        String name;
    }

    private static final String METHOD_NAME_PROVIDES = "provides%s";

    private final Map<String, Integer> mProvidesMethodNames = new HashMap<>();
    private Class<? extends Annotation> mScope;

    public ModuleBuilder(Class<? extends Annotation> scope, GCN genClassName, ClassName className) throws ProcessorError {
        super(genClassName, className, GPN.KNIGHT, GPN.DI, GPN.MODULES);
        if (scope != AppScope.class && scope != ScreenScope.class && scope != ActivityScope.class) {
            throw new IllegalStateException("Unsupported Scope class.");
        }
        mScope = scope;
    }

    public ModuleBuilder(Class<? extends Annotation> scope, GCN genClassName) throws ProcessorError {
        this(scope, genClassName, null);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        getBuilder().addAnnotation(Module.class);
    }

    public void addProvidesConstructor(ExecutableElement e) throws ProcessorError {
        if (!e.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessorError(e, ErrorMsg.Provided_constructor_has_to_be_public);
        }
        Attr attr = getAnnotationAttributes(e);

        ClassName className = ClassName.get((TypeElement) e.getEnclosingElement());

        MethodSpec.Builder method = prepareProvidesMethodBuilder(className.simpleName(), attr);
        method.addStatement(buildProvideStatement(e, method, "new $T"), className);
        method.returns(className);

        getBuilder().addMethod(method.build());
    }

    public void addProvidesMethod(ExecutableElement e) throws ProcessorError {
        if (!e.getModifiers().containsAll(Arrays.asList(Modifier.PUBLIC, Modifier.STATIC))) {
            throw new ProcessorError(e, ErrorMsg.Provided_method_has_to_be_public_static);
        }
        Attr attr = getAnnotationAttributes(e);

        ClassName className = ClassName.get((TypeElement) e.getEnclosingElement());
        TypeName returnTypeName = ClassName.get(e.getReturnType());

        MethodSpec.Builder method = prepareProvidesMethodBuilder(e.getSimpleName().toString(), attr);
        method.addStatement(buildProvideStatement(e, method, "$T.$N"), className, e.getSimpleName().toString());
        method.returns(returnTypeName);

        getBuilder().addMethod(method.build());
    }

    private MethodSpec.Builder prepareProvidesMethodBuilder(String methodNamePart, Attr attr) {
        String methodName = formatProvidesName(methodNamePart, attr);

        MethodSpec.Builder method = MethodSpec.methodBuilder(methodName)
                .addAnnotation(Provides.class)
                .addModifiers(Modifier.PUBLIC);

        if (attr.scoped) {
            method.addAnnotation(mScope);
        }
        if (attr.name != null) {
            method.addAnnotation(
                    AnnotationSpec.builder(Named.class).addMember("value", "$S", attr.name).build()
            );
        }
        return method;
    }

    private String formatProvidesName(final String methodName, Attr attr) {
        String name = String.format(METHOD_NAME_PROVIDES, StringUtils.startUpperCase(methodName));
        if (attr.name != null) {
            name += StringUtils.startUpperCase(attr.name);
        }
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

    private String buildProvideStatement(ExecutableElement e, MethodSpec.Builder method, String call) {
        StringBuilder statement = new StringBuilder();
        statement.append(String.format("return %s(", call));
        for (int i = 0; i < e.getParameters().size(); i++) {
            VariableElement p = e.getParameters().get(i);
            Set<Modifier> modifiers = p.getModifiers();

            statement.append(p.getSimpleName().toString());
            if (i < e.getParameters().size() - 1) {
                statement.append(", ");
            }

            ParameterSpec.Builder param = ParameterSpec.builder(
                    ClassName.get(p.asType()),
                    p.getSimpleName().toString(),
                    modifiers.toArray(new Modifier[modifiers.size()])
            );
            method.addParameter(param.build());
        }
        statement.append(")");
        return statement.toString();
    }

    private Attr getAnnotationAttributes(Element e) {
        Attr attr = new Attr();
        if (mScope == AppScope.class) {
            AppProvided a = e.getAnnotation(AppProvided.class);
            attr.scoped = a.scoped();
            attr.name = a.named();
        } else if (mScope == ScreenScope.class) {
            ScreenProvided a = e.getAnnotation(ScreenProvided.class);
            attr.scoped = a.scoped();
            attr.name = a.named();
        } else if (mScope == ActivityScope.class) {
            ActivityProvided a = e.getAnnotation(ActivityProvided.class);
            attr.scoped = a.scoped();
            attr.name = a.named();
        }
        if (attr.name == null || attr.name.isEmpty()) {
            attr.name = null;
        }
        return attr;
    }

}
