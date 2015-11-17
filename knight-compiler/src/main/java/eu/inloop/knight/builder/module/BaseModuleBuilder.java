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
import eu.inloop.knight.As;
import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.builder.BaseClassBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.builder.GPN;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link BaseModuleBuilder} is used for creating Module class file.
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public abstract class BaseModuleBuilder extends BaseClassBuilder {

    /**
     * Format of provide method name.
     */
    private static final String FORMAT_METHOD_NAME_PROVIDES = "provides%s";

    private final Map<String, Integer> mProvidesMethodNames = new HashMap<>();
    private Class<? extends Annotation> mScope;

    /**
     * Constructor
     *
     * @param scope        Module scope
     * @param genClassName Name of generated module class.
     * @param className    Class name that will be used when formatting generated module class name.
     */
    public BaseModuleBuilder(Class<? extends Annotation> scope, GCN genClassName, ClassName className) throws ProcessorError {
        super(genClassName, className, GPN.KNIGHT, GPN.DI, GPN.MODULES);
        if (scope != AppScope.class && scope != ScreenScope.class && scope != ActivityScope.class) {
            throw new IllegalStateException("Unsupported Scope class.");
        }
        mScope = scope;
        addScopeSpecificPart();
    }

    /**
     * Constructor
     *
     * @param scope        Module scope
     * @param genClassName Name of generated module class.
     */
    public BaseModuleBuilder(Class<? extends Annotation> scope, GCN genClassName) throws ProcessorError {
        this(scope, genClassName, null);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        getBuilder().addAnnotation(Module.class);
    }

    /**
     * Adds method for providing given constructor.
     *
     * @param e Constructor element that will be called in <code>provides</code> method.
     */
    public void addProvidesConstructor(ExecutableElement e) throws ProcessorError {
        if (!e.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessorError(e, ErrorMsg.Provided_constructor_not_public);
        }

        ClassName className = ClassName.get((TypeElement) e.getEnclosingElement());

        MethodSpec.Builder method = prepareProvidesMethodBuilder(e, className.simpleName(), isScoped(e));
        addProvideStatement(method, e, "new $T", className);
        method.returns(getProvidedType(e, className));

        getBuilder().addMethod(method.build());
    }

    /**
     * Adds method for providing given method.
     *
     * @param e Method element that will be called in <code>provides</code> method.
     */
    public void addProvidesMethod(ExecutableElement e) throws ProcessorError {
        if (!e.getModifiers().containsAll(Arrays.asList(Modifier.PUBLIC, Modifier.STATIC))) {
            throw new ProcessorError(e, ErrorMsg.Provided_method_not_public_static);
        }

        ClassName className = ClassName.get((TypeElement) e.getEnclosingElement());

        MethodSpec.Builder method = prepareProvidesMethodBuilder(e, e.getSimpleName().toString(), isScoped(e));
        addProvideStatement(method, e, "$T.$N", className, e.getSimpleName().toString());
        method.returns(ClassName.get(e.getReturnType()));

        getBuilder().addMethod(method.build());
    }

    private TypeName getProvidedType(ExecutableElement e, TypeName defaultType) {
        As as = e.getAnnotation(As.class);
        if (as != null) {
            // TODO : add some check for superclass
            return ProcessorUtils.getType(as, new ProcessorUtils.IGetter<As, Class<?>>() {
                @Override
                public Class<?> get(As annotation) {
                    return annotation.value();
                }
            });
        } else {
            return defaultType;
        }
    }

    /**
     * Prepares {@link MethodSpec.Builder} for <code>provides</code> method.
     *
     * @param e              Method or Constructor element that will be called in <code>provides</code> method.
     * @param methodNamePart Name of the <code>provides</code> method that will be formatted.
     */
    protected MethodSpec.Builder prepareProvidesMethodBuilder(ExecutableElement e, String methodNamePart) {
        return prepareProvidesMethodBuilder(e, methodNamePart, false);
    }

    /**
     * Prepares {@link MethodSpec.Builder} for <code>provides</code> method.
     *
     * @param e              Method or Constructor element that will be called in <code>provides</code> method.
     * @param methodNamePart Name of the <code>provides</code> method that will be formatted.
     * @param scoped         <code>true</code> if <code>provides</code> method will be scoped.
     */
    protected MethodSpec.Builder prepareProvidesMethodBuilder(ExecutableElement e, String methodNamePart, boolean scoped) {
        String methodName = createProvideMethodName(methodNamePart);

        MethodSpec.Builder method = MethodSpec.methodBuilder(methodName)
                .addAnnotation(Provides.class)
                .addModifiers(Modifier.PUBLIC);

        if (scoped) {
            method.addAnnotation(mScope);
        }
        // add also Qualifier annotations
        for (AnnotationSpec a : getQualifiers(e)) {
            method.addAnnotation(a);
        }
        return method;
    }

    /**
     * Formats given method name to create <code>provides</code> method name. Ensures that 2 methods won't have same name.
     */
    protected String createProvideMethodName(final String methodName) {
        String name = String.format(FORMAT_METHOD_NAME_PROVIDES, StringUtils.startUpperCase(methodName));

        int count = trackMethodName(name);
        if (count > 1) name += count;

        return name;
    }

    /**
     * Tracks count of methods with given name.
     *
     * @param name Method name.
     */
    private int trackMethodName(final String name) {
        Integer i = mProvidesMethodNames.get(name);
        if (i == null) i = 0;
        i++;
        mProvidesMethodNames.put(name, i);
        return i;
    }

    /**
     * Adds statement to given <code>method</code>.
     *
     * @param method     Method Builder.
     * @param e          Element which will be provided. (Constructor or method)
     * @param callFormat Format for calling the element <code>e</code>.
     * @param args       Argument for the <code>callFormat</code>.
     */
    protected void addProvideStatement(MethodSpec.Builder method, ExecutableElement e, String callFormat, Object... args) {
        addProvideCode(true, method, e, callFormat, args);
    }

    /**
     * Adds code to given <code>method</code>.
     *
     * @param asReturnStatement <code>true</code> if there should be <code>return</code> in added code.
     * @param method            Method Builder.
     * @param e                 Element which will be provided. (Constructor or method)
     * @param callFormat        Format for calling the element <code>e</code>.
     * @param args              Argument for the <code>callFormat</code>.
     */
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
            // add also Qualifier annotations
            for (AnnotationSpec a : getQualifiers(p)) {
                param.addAnnotation(a);
            }
            method.addParameter(param.build());
        }
        method.addCode(")");
        if (asReturnStatement) method.addCode(";\n");
    }

    /**
     * Adds scope specific part of the generated module.
     */
    protected abstract void addScopeSpecificPart();

    /**
     * Determines <code>provides</code> method for given element will be scoped.
     *
     * @param e Element.
     */
    protected abstract boolean isScoped(Element e);

}
