package eu.inloop.knight.builder;

import android.util.Pair;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.component.ActivityComponentBuilder;
import eu.inloop.knight.builder.component.AppComponentBuilder;
import eu.inloop.knight.builder.component.BaseComponentBuilder;
import eu.inloop.knight.builder.component.ScreenComponentBuilder;
import eu.inloop.knight.builder.module.ActivityModuleBuilder;
import eu.inloop.knight.builder.module.AppModuleBuilder;
import eu.inloop.knight.builder.module.ScreenModuleBuilder;
import eu.inloop.knight.core.IActivityComponent;
import eu.inloop.knight.core.IScreenComponent;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link ComponentFactoryBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public class ComponentFactoryBuilder extends BaseClassBuilder {

    public static final String METHOD_NAME_BUILD = "build";
    public static final String METHOD_NAME_BUILD_AND_INJECT = "buildAndInject";

    public ComponentFactoryBuilder(ClassName className) throws ProcessorError {
        super(GCN.COMPONENT_FACTORY, className, GPN.KNIGHT, GPN.DI, GPN.FACTORIES);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    public void addBuildMethod(AppComponentBuilder componentBuilder, AppModuleBuilder mainModuleBuilder) {
        String app = "app";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(EClass.Application.getName(), app, Modifier.FINAL)
                .returns(componentBuilder.getClassName());

        // get component builder
        method.addCode("return $T.builder()", EClass.DaggerApplicationComponent.getName());
        ClassName module;
        for (int i = 0; i < componentBuilder.getModules().size(); i++) {
            method.addCode("\n");
            module = componentBuilder.getModules().get(i);
            if (module.equals(mainModuleBuilder.getClassName())) {
                // main module with Application parameter
                method.addCode("\t.$N(new $T($N))",
                        StringUtils.startLowerCase(module.simpleName()), module, app);
            } else {
                // other modules with empty constructor
                method.addCode("\t.$N(new $T())",
                        StringUtils.startLowerCase(module.simpleName()), module);
            }
        }
        // build component
        method.addCode("\n\t.build();\n");

        getBuilder().addMethod(method.build());
    }

    private void addBuildMethod(AppComponentBuilder parentComponentBuilder, ScreenComponentBuilder componentBuilder, ScreenModuleBuilder mainModuleBuilder) {
        String appC = "appComponent";
        String stateManager = "stateManager";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parentComponentBuilder.getClassName(), appC, Modifier.FINAL)
                .addParameter(EClass.StateManager.getName(), stateManager, Modifier.FINAL)
                .returns(componentBuilder.getClassName());

        method.addCode("return $N.plus(\n", appC);
        ClassName module;
        for (int i = 0; i < componentBuilder.getModules().size(); i++) {
            if (i > 0) {
                method.addCode(",\n");
            }
            module = componentBuilder.getModules().get(i);
            if (module.equals(mainModuleBuilder.getClassName()) || componentBuilder.isExtended(module)) {
                // main module + extended modules with Bundle parameter
                method.addCode("\tnew $T($N)", module, stateManager);
            } else {
                // other modules with empty constructor
                method.addCode("\tnew $T()", module);
            }
        }
        method.addCode("\n);\n");

        getBuilder().addMethod(method.build());
    }

    private void addBuildMethod(ScreenComponentBuilder parentComponentBuilder, ActivityComponentBuilder componentBuilder, ActivityModuleBuilder mainModuleBuilder) {
        String sc = "screenComponent";
        String activity = "activity";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parentComponentBuilder.getClassName(), sc, Modifier.FINAL)
                .addParameter(EClass.Activity.getName(), activity, Modifier.FINAL)
                .returns(componentBuilder.getClassName());

        method.addCode("return $N.plus(\n", sc);
        ClassName module;
        for (int i = 0; i < componentBuilder.getModules().size(); i++) {
            if (i > 0) {
                method.addCode(",\n");
            }
            module = componentBuilder.getModules().get(i);
            if (module.equals(mainModuleBuilder.getClassName())) {
                // main module with Activity parameter
                method.addCode("\tnew $T($N)", module, activity);
            } else {
                // other modules with empty constructor
                method.addCode("\tnew $T()", module);
            }
        }
        method.addCode("\n);\n");

        getBuilder().addMethod(method.build());
    }

    public void addBuildMethods(AppComponentBuilder appcBuilder, ScreenComponentBuilder scBuilder, ScreenModuleBuilder smBuilder, ActivityComponentBuilder acBuilder, ActivityModuleBuilder amBuilder) {
        addBuildMethod(appcBuilder, scBuilder, smBuilder);
        addBuildMethod(scBuilder, acBuilder, amBuilder);

        String appC = "appComponent";
        String sc = "screenComponent";
        String localSc = "sc";
        String ac = "activityComponent";
        String stateManager = "stateManager";
        String activity = "activity";

        MethodSpec method = MethodSpec.methodBuilder(METHOD_NAME_BUILD_AND_INJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(appcBuilder.getClassName(), appC, Modifier.FINAL)
                .addParameter(IScreenComponent.class, sc, Modifier.FINAL)
                .addParameter(getArgClassName(), activity, Modifier.FINAL)
                .addParameter(EClass.StateManager.getName(), stateManager, Modifier.FINAL)
                .returns(ParameterizedTypeName.get(Pair.class, IScreenComponent.class, IActivityComponent.class))
                .addStatement("$T $N", scBuilder.getClassName(), localSc)
                .addCode("// create Screen Component if necessary\n")
                .beginControlFlow("if ($N == null)", sc)
                .addStatement("$N = $N($N, $N)", localSc, METHOD_NAME_BUILD, appC, stateManager)
                .endControlFlow()
                .beginControlFlow("else")
                .addStatement("$N = ($T) $N", localSc, scBuilder.getClassName(), sc)
                .endControlFlow()
                .addCode("// create Activity Component\n")
                .addStatement("$T $N = $N($N, $N)", acBuilder.getClassName(), ac, METHOD_NAME_BUILD, localSc, activity)
                .addCode("// inject the Activity\n")
                .addStatement("$N.$N($N)", ac, BaseComponentBuilder.METHOD_NAME_INJECT, activity)
                .addStatement("return new $T($N, $N)", ParameterizedTypeName.get(Pair.class, IScreenComponent.class, IActivityComponent.class), localSc, ac)
                .build();

        getBuilder().addMethod(method);
    }
}
