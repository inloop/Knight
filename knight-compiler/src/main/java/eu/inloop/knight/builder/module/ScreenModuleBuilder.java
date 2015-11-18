package eu.inloop.knight.builder.module;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import dagger.Provides;
import eu.inloop.knight.NamedExtra;
import eu.inloop.knight.PresenterPool;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.builder.NavigatorBuilder;
import eu.inloop.knight.core.StateManager;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link ScreenModuleBuilder} is used for creating Screen Module class file.
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class ScreenModuleBuilder extends BaseModuleBuilder {

    private static final String METHOD_NAME_INIT = "init";
    private static final String FIELD_NAME_STATE_MANAGER = "mStateManager";
    private static final String STATEFUL_ID_FORMAT = "[%s]%s";

    /**
     * Constructor
     *
     * @param className Class name of Activity for which this module will be generated.
     */
    public ScreenModuleBuilder(ClassName className) throws ProcessorError {
        super(ScreenScope.class, GCN.SCREEN_MODULE, className);
    }

    /**
     * Constructor
     *
     * @param genClassName Name of generated module class.
     * @param className    Class name of Activity for which this module will be generated.
     */
    public ScreenModuleBuilder(GCN genClassName, ClassName className) throws ProcessorError {
        super(ScreenScope.class, genClassName, className);
    }

    @Override
    protected void addScopeSpecificPart() {
        // Application attribute
        FieldSpec stateField = FieldSpec.builder(StateManager.class, FIELD_NAME_STATE_MANAGER,
                Modifier.PRIVATE, Modifier.FINAL).build();
        getBuilder().addField(stateField);
        // constructor
        String stateManager = "stateManager";
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(StateManager.class, stateManager)
                .addStatement("$N = $N", stateField, stateManager);
        addToConstructor(constructor);
        getBuilder().addMethod(constructor.build());
        // add provides-method for Presenter Pool
        addProvidesPresenterPool();
    }

    private void addProvidesPresenterPool() {
        ClassName className = ClassName.get(PresenterPool.class);

        MethodSpec.Builder method = MethodSpec.methodBuilder(createProvideMethodName(className.simpleName()))
                .addAnnotation(Provides.class)
                .addAnnotation(ScreenScope.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(className);

        method.addStatement("return $N.manage($S, new $T())",
                FIELD_NAME_STATE_MANAGER, createStateManagerId(method.build()), className);

        getBuilder().addMethod(method.build());
    }

    /**
     * Adds additional parameters or statements to constructor.
     *
     * @param constructor Constructor builder.
     */
    protected void addToConstructor(MethodSpec.Builder constructor) {
        String activity = "activity";
        constructor.addParameter(Activity.class, activity)
                .addStatement("$N($N)", METHOD_NAME_INIT, activity);
    }

    @Override
    protected void addProvideStatement(MethodSpec.Builder method, ExecutableElement e, String callFormat, Object... args) {
        MethodSpec m = method.build();
        // check if is screen scoped
        if (m.annotations.contains(AnnotationSpec.builder(ScreenScope.class).build())) {
            // manage state only if scoped
            method.addCode("return $N.manage($S, ", FIELD_NAME_STATE_MANAGER, createStateManagerId(m));
            addProvideCode(false, method, e, callFormat, args);
            method.addCode(");\n");
        } else {
            addProvideCode(true, method, e, callFormat, args);
        }
    }

    private String createStateManagerId(MethodSpec m) {
        return String.format(STATEFUL_ID_FORMAT, m.returnType, m.name);
    }

    /**
     * Adds <code>init</code> method to the generated module.
     *
     * @param extraFields List of Extras that will be initialized.
     */
    public void addInitMethod(List<NamedExtra> extraFields) {
        String activity = "activity";
        String extras = "extras";
        String action = "action";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_INIT)
                .addModifiers(Modifier.PRIVATE)
                .addParameter(Activity.class, activity);

        // do not retrieve extras if empty
        if (!extraFields.isEmpty()) {
            method.addStatement("$T $N = $N.getIntent().getExtras()", Bundle.class, extras, activity);
            method.addStatement("$T $N = $N.getIntent().getAction()", String.class, action, activity);
            method.beginControlFlow("if ($N != null)", extras);

            for (NamedExtra namedExtra : extraFields) {
                TypeName typeName = ClassName.get(namedExtra.getElement().asType());
                String name = namedExtra.getName();
                // add field
                FieldSpec field = FieldSpec.builder(typeName, createFieldName(name), Modifier.PRIVATE).build();
                getBuilder().addField(field);
                // add statement to init method
                String extraId = NavigatorBuilder.getExtraId(getArgClassName(), name);
                method.addStatement("$N = ($T) $N.$N($S)",
                        field, typeName, extras, getExtraGetterName(typeName), extraId);

                addProvidesField(namedExtra, field);
            }

            method.endControlFlow()
                    .beginControlFlow("else if ($N != null && $N.equals($S))", action, action, "android.intent.action.MAIN")
                    .addCode("// MAIN won't have any extras\n")
                    .addStatement("return")
                    .endControlFlow()
                    .beginControlFlow("else")
                    .addCode("// throw exception if Intent was wrongly created\n")
                    .addStatement("throw new $T($S)", IllegalStateException.class, "Extras were not set")
                    .endControlFlow();

        }

        getBuilder().addMethod(method.build());
    }

    /**
     * Adds <code>provides</code> method for given <code>field</code>.
     *
     * @param namedExtra Extra that defines the <code>field</code>.
     * @param field      Field.
     */
    private void addProvidesField(NamedExtra namedExtra, FieldSpec field) {
        MethodSpec.Builder method = MethodSpec.methodBuilder(createProvideMethodName(namedExtra.getName()))
                .addAnnotation(Provides.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(field.type)
                .addStatement("return $N", field);

        // add also Qualifier annotations
        for (AnnotationSpec a : getQualifiers(namedExtra.getElement())) {
            method.addAnnotation(a);
        }

        getBuilder().addMethod(method.build());
    }

    /**
     * Returns getter for given Extra type.
     */
    private String getExtraGetterName(TypeName typeName) {
        if (typeName.isPrimitive()) {
            return String.format("get%s", StringUtils.startUpperCase(typeName.toString()));
        } else {
            return "get";
        }
    }

}
