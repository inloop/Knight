package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.scope.ScreenScope;
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

    public void addBuildMethod(ComponentBuilder componentBuilder, ModuleBuilder mainModuleBuilder) {
        addBuildMethod(null, componentBuilder, mainModuleBuilder);
    }

    public void addBuildMethod(ComponentBuilder parentComponentBuilder, ComponentBuilder componentBuilder, ModuleBuilder mainModuleBuilder) {
        MethodSpec.Builder method;
        if (componentBuilder.getScope() == AppScope.class) {
            method = buildAppC(componentBuilder, mainModuleBuilder);
        } else if (componentBuilder.getScope() == ScreenScope.class) {
            method = buildSC(parentComponentBuilder, componentBuilder, mainModuleBuilder);
        } else {
            method = buildAC(parentComponentBuilder, componentBuilder, mainModuleBuilder);
        }

        method.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        method.returns(componentBuilder.getClassName());

        getBuilder().addMethod(method.build());
    }

    private MethodSpec.Builder buildAppC(ComponentBuilder componentBuilder, ModuleBuilder mainModuleBuilder) {
        String app = "app";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD_APPC)
                .addParameter(EClass.Application.getName(), app);

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

        return method;
    }

    private MethodSpec.Builder buildSC(ComponentBuilder parentComponentBuilder, ComponentBuilder componentBuilder, ModuleBuilder mainModuleBuilder) {
        String appC = "appComponent";
        String bundle = "bundle";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD_SC)
                .addParameter(parentComponentBuilder.getClassName(), appC)
                .addParameter(EClass.Bundle.getName(), bundle);

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

        return method;
    }

    private MethodSpec.Builder buildAC(ComponentBuilder parentComponentBuilder, ComponentBuilder componentBuilder, ModuleBuilder mainModuleBuilder) {
        String sc = "screenComponent";
        String activity = "activity";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_BUILD_AC)
                .addParameter(parentComponentBuilder.getClassName(), sc)
                .addParameter(EClass.Activity.getName(), activity);

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

        return method;
    }

}
