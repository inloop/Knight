package eu.inloop.knight.builder.module;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.inject.Named;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import dagger.Provides;
import eu.inloop.knight.EClass;
import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.With;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.builder.NavigatorBuilder;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link ScreenModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class ScreenModuleBuilder extends BaseModuleBuilder {

    private static final String METHOD_NAME_INIT = "init";
    private static final String FIELD_NAME_INIT = "m%s";
    private static final String METHOD_NAME_PROVIDES_FIELD = "providesField%s";
    private static final String FIELD_NAME_STATE_MANAGER = "mStateManager";
    private static final String STATEFUL_ID_FORMAT = "[%s]%s";

    public ScreenModuleBuilder(ClassName className) throws ProcessorError {
        super(ScreenScope.class, GCN.SCREEN_MODULE, className);
    }

    public ScreenModuleBuilder(GCN genClassName, ClassName className) throws ProcessorError {
        super(ScreenScope.class, genClassName, className);
    }

    @Override
    protected void addScopeSpecificPart() {
        // Application attribute
        FieldSpec stateField = FieldSpec.builder(EClass.StateManager.getName(), FIELD_NAME_STATE_MANAGER,
                Modifier.PRIVATE, Modifier.FINAL).build();
        getBuilder().addField(stateField);
        // constructor
        String stateManager = "stateManager";
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(EClass.StateManager.getName(), stateManager)
                .addStatement("$N = $N", stateField, stateManager);
        addToConstructor(constructor);
        getBuilder().addMethod(constructor.build());
    }

    protected void addToConstructor(MethodSpec.Builder method) {
        String activity = "activity";
        method.addParameter(Activity.class, activity)
                .addStatement("$N($N)", METHOD_NAME_INIT, activity);
    }

    @Override
    protected boolean isScoped(Element e) {
        return e.getAnnotation(ScreenProvided.class).scoped();
    }

    @Override
    protected void addProvideStatement(MethodSpec.Builder method, ExecutableElement e, String callFormat, Object... args) {
        MethodSpec m = method.build();
        // check if is screen scoped
        if (m.annotations.contains(AnnotationSpec.builder(ScreenScope.class).build())) {
            // manage state only if scoped
            method.addCode("return $N.manage($S, ", FIELD_NAME_STATE_MANAGER, String.format(STATEFUL_ID_FORMAT, m.returnType, m.name));
            addProvideCode(false, method, e, callFormat, args);
            method.addCode(");\n");
        } else {
            addProvideCode(true, method, e, callFormat, args);
        }
    }

    public void addInitMethod(With[] withParams) {
        String activity = "activity";
        String extras = "extras";
        String action = "action";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_INIT)
                .addModifiers(Modifier.PRIVATE)
                .addParameter(Activity.class, activity);

        // do not retrieve extras if empty
        if (withParams.length > 0) {
            method.addStatement("$T $N = $N.getIntent().getExtras()", Bundle.class, extras, activity);
            method.addStatement("$T $N = $N.getIntent().getAction()", String.class, action, activity);
            method.beginControlFlow("if ($N != null)", extras)
                    .endControlFlow()
                    .beginControlFlow("else if ($N != null && $N.equals($S))", action, action, "android.intent.action.MAIN")
                    .addCode("// MAIN won't have any extras\n")
                    .addStatement("return")
                    .endControlFlow()
                    .beginControlFlow("else")
                    .addCode("// throw exception if Intent was wrongly created\n")
                    .addStatement("throw new $T($S)", IllegalStateException.class, "Extras were not set")
                    .endControlFlow();
        }

        for (With with : withParams) {
            TypeName typeName = ProcessorUtils.getType(with, new ProcessorUtils.IGetter<With, Class<?>>() {
                @Override
                public Class<?> get(With obj) {
                    return obj.type();
                }
            });
            // add field
            FieldSpec field = FieldSpec.builder(typeName, getInitFieldName(with), Modifier.PRIVATE).build();
            getBuilder().addField(field);
            // add statement to init method
            String extraId = NavigatorBuilder.getExtraId(getArgClassName(), with.name());
            method.addStatement("$N = ($T) $N.$N($S)",
                    field, typeName, extras, getExtraGetterName(typeName), extraId);

            addProvidesField(with, field);
        }

        getBuilder().addMethod(method.build());
    }

    private void addProvidesField(With with, FieldSpec field) {
        // TODO : allow @Nullable provides methods ?

        MethodSpec.Builder method = MethodSpec.methodBuilder(getProvidesFieldMethodName(with))
                .addAnnotation(Provides.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(field.type)
                .addStatement("return $N", field);

        if (with.withNamed().length() > 0) {
            method.addAnnotation(AnnotationSpec.builder(Named.class).addMember("value", "$S", with.withNamed()).build());
        }

        getBuilder().addMethod(method.build());
    }

    private String getExtraGetterName(TypeName typeName) {
        if (typeName.isPrimitive()) {
            return String.format("get%s", StringUtils.startUpperCase(typeName.toString()));
        } else {
            return "get";
        }
    }

    private String getProvidesFieldMethodName(With with) {
        return String.format(METHOD_NAME_PROVIDES_FIELD, StringUtils.startUpperCase(with.name()));
    }

    private String getInitFieldName(With with) {
        return String.format(FIELD_NAME_INIT, StringUtils.startUpperCase(with.name()));
    }
}
