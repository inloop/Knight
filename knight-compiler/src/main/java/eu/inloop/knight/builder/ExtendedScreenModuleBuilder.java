package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;

import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ExtendedScreenModuleBuilder} is used for extending Screen-scoped Module class.
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class ExtendedScreenModuleBuilder extends ModuleBuilder {

    private final TypeElement mSuperClass;

    public ExtendedScreenModuleBuilder(TypeElement superClass) throws ProcessorError {
        super(ScreenScope.class, GCN.EXTENDED_MODULE, ClassName.get(superClass));
        mSuperClass = superClass;
    }

    @Override
    public void end() throws ProcessorError {
        super.end();
        // set super class
        getBuilder().superclass(ClassName.get(mSuperClass));
        // TODO : override methods for save state mechanism
    }
}
