package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.component.ActivityComponentBuilder;
import eu.inloop.knight.builder.component.AppComponentBuilder;
import eu.inloop.knight.builder.component.ScreenComponentBuilder;
import eu.inloop.knight.builder.module.ActivityModuleBuilder;
import eu.inloop.knight.builder.module.AppModuleBuilder;
import eu.inloop.knight.builder.module.ScreenModuleBuilder;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link ComponentFactoryBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public class ComponentFactoryBuilder extends BaseClassBuilder {

    private static final String METHOD_NAME_BUILD_APPC = "buildComponent";
    private static final String METHOD_NAME_BUILD_SC = "buildScreenComponent";
    private static final String METHOD_NAME_BUILD_AC = "buildActivityComponent";

    public ComponentFactoryBuilder(ClassName componentName) throws ProcessorError {
        super(GCN.COMPONENT_FACTORY, componentName, GPN.KNIGHT, GPN.DI, GPN.FACTORIES);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    public void addBuildMethod(AppComponentBuilder componentBuilder, AppModuleBuilder mainModuleBuilder) {
        String app = "app";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD_APPC)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(EClass.Application.getName(), app)
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

    public void addBuildMethod(AppComponentBuilder parentComponentBuilder, ScreenComponentBuilder componentBuilder, ScreenModuleBuilder mainModuleBuilder) {
        String appC = "appComponent";
        String bundle = "bundle";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD_SC)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(parentComponentBuilder.getClassName(), appC)
                .addParameter(EClass.Bundle.getName(), bundle)
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
                method.addCode("\tnew $T($N)", module, bundle);
            } else {
                // other modules with empty constructor
                method.addCode("\tnew $T()", module);
            }
        }
        method.addCode("\n);\n");

        getBuilder().addMethod(method.build());
    }

    public void addBuildMethod(ScreenComponentBuilder parentComponentBuilder, ActivityComponentBuilder componentBuilder, ActivityModuleBuilder mainModuleBuilder) {
        String sc = "screenComponent";
        String activity = "activity";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD_AC)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(parentComponentBuilder.getClassName(), sc)
                .addParameter(EClass.Activity.getName(), activity)
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

}
