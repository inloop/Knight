package eu.inloop.knight.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;

/**
 * Class {@link BaseComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public abstract class BaseComponentBuilder extends BaseClassBuilder {

    private static final String METHOD_NAME_INJECT = "inject";
    private static final String METHOD_NAME_PLUS = "plus";

    private final EClass mScope;
    private final List<ClassName> mModules = new ArrayList<>();

    public BaseComponentBuilder(EClass scope, GCN genClassName, ClassName className) throws ProcessorError {
        super(false, genClassName, className, GPN.KNIGHT, GPN.DI, GPN.COMPONENTS);
        mScope = scope;
    }

    public BaseComponentBuilder(EClass scope, GCN genClassName) throws ProcessorError {
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
        getBuilder().addAnnotation(mScope.getName());

        // add Component Annotation
        StringBuilder modulesFormat = new StringBuilder();
        modulesFormat.append("{").append("\n");
        for (int i = 0; i < mModules.size(); i++) {
            modulesFormat.append("$T.class,").append("\n");
        }
        modulesFormat.append("}");

        EClass a = (mScope == EClass.AppScope) ? EClass.Component : EClass.Subcomponent;
        getBuilder().addAnnotation(
                AnnotationSpec.builder(a.getName())
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
