package eu.inloop.knight.builder.module;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import dagger.Provides;
import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link ExtendedScreenModuleBuilder} is used for creating Extended Screen Module class file.
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class ExtendedScreenModuleBuilder extends ScreenModuleBuilder {

    /**
     * Format of <code>managed</code> method.
     */
    private static final String FORMAT_METHOD_NAME_MANAGED = "managed%s";

    private final TypeElement mSuperClass;
    private final List<ExecutableElement> mScopedMethods;

    /**
     * Constructor
     *
     * @param superClass    Name of Class that will be extended by generated module.
     * @param scopedMethods List of scoped methods from superclass.
     */
    public ExtendedScreenModuleBuilder(TypeElement superClass, List<ExecutableElement> scopedMethods) throws ProcessorError {
        super(GCN.EXTENDED_MODULE, ClassName.get(superClass));
        mSuperClass = superClass;
        mScopedMethods = scopedMethods;
    }

    @Override
    protected void addToConstructor(MethodSpec.Builder constructor) {
        // do not add anything
    }

    @Override
    public void end() throws ProcessorError {
        super.end();
        // set super class
        getBuilder().superclass(ClassName.get(mSuperClass));
        // override methods for save state mechanism
        for (ExecutableElement methodElement : mScopedMethods) {
            addManagedMethod(methodElement);
        }
    }

    /**
     * Adds <code>provides</code> method for scoped method from superclass.
     *
     * @param methodElement Scoped method from superclass.
     */
    private void addManagedMethod(ExecutableElement methodElement) throws ProcessorError {
        // cannot have @Provides annotation
        if (methodElement.getAnnotation(Provides.class) != null) {
            throw new ProcessorError(methodElement, ErrorMsg.Screen_Scoped_module_method_with_Provides);
        }
        String scopedMethodName = methodElement.getSimpleName().toString();
        MethodSpec.Builder method = prepareProvidesMethodBuilder(
                methodElement, createManagedMethodName(scopedMethodName))
                .returns(ClassName.get(methodElement.getReturnType()));

        addProvideStatement(method, methodElement, "super.$N", scopedMethodName);

        getBuilder().addMethod(method.build());
    }

    private String createManagedMethodName(String scopedMethodName) {
        return String.format(FORMAT_METHOD_NAME_MANAGED, StringUtils.startUpperCase(scopedMethodName));
    }

}
