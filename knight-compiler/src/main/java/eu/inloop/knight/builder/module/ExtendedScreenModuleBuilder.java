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
 * Class {@link ExtendedScreenModuleBuilder} is used for extending Screen-scoped Module class.
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class ExtendedScreenModuleBuilder extends ScreenModuleBuilder {

    private final TypeElement mSuperClass;
    private final List<ExecutableElement> mScopedMethods;

    public ExtendedScreenModuleBuilder(TypeElement superClass, List<ExecutableElement> scopedMethods) throws ProcessorError {
        super(GCN.EXTENDED_MODULE, ClassName.get(superClass));
        mSuperClass = superClass;
        mScopedMethods = scopedMethods;
    }

    @Override
    protected void addToConstructor(MethodSpec.Builder method) {
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

    private void addManagedMethod(ExecutableElement methodElement) throws ProcessorError {
        // cannot have @Provides annotation
        if (methodElement.getAnnotation(Provides.class) != null) {
            throw new ProcessorError(methodElement, ErrorMsg.Screen_Scoped_module_method_with_Provides);
        }

        MethodSpec.Builder method = prepareProvidesMethodBuilder(
                methodElement, "managed" + StringUtils.startUpperCase(methodElement.getSimpleName().toString()))
                .returns(ClassName.get(methodElement.getReturnType()));

        addProvideStatement(method, methodElement, "super.$N", methodElement.getSimpleName().toString());

        getBuilder().addMethod(method.build());
    }

}
