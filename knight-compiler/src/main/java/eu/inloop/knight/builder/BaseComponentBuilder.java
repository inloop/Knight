package eu.inloop.knight.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link BaseComponentBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public abstract class BaseComponentBuilder extends BaseClassBuilder {

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

        getBuilder().addAnnotation(AnnotationSpec.builder(
                ((mScope == EClass.AppScope) ? EClass.Component : EClass.Subcomponent).getName())
                .addMember("modules", modulesFormat.toString(), mModules.toArray())
                .build());
    }

    protected List<ClassName> getmModules() {
        return mModules;
    }

    public void addModule(ClassName module) {
        mModules.add(module);
    }

}
