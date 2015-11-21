package eu.inloop.knight.builder;

import android.app.Activity;
import android.app.Application;
import android.util.Pair;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Collection;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.core.ComponentStorage;
import eu.inloop.knight.core.IActivityComponent;
import eu.inloop.knight.core.IScreenComponent;
import eu.inloop.knight.core.StateManager;
import eu.inloop.knight.name.GCN;
import eu.inloop.knight.name.GPN;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link KnightBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-15
 */
public class KnightBuilder extends BaseClassBuilder {

    /**
     * Overridden method with parameters: (Activity activity, StateManager manager, IScreenComponent sc)
     */
    private static final String METHOD_NAME_ON_BUILD_AND_INJECT = "buildComponentsAndInject";
    /**
     * Overridden method with parameters: (Class activityClass)
     */
    private static final String METHOD_NAME_IS_SCOPED = "isScoped";

    private static final String METHOD_NAME_GET_APPC = "getApplicationComponent";
    private static final String METHOD_NAME_GET_AC = "getActivityComponent";

    public static final String METHOD_NAME_INIT = "braceYourselfFor";
    public static final String METHOD_NAME_FROM = "from";
    public static final String METHOD_NAME_FROM_APP = "fromApp";

    private static final String METHOD_NAME_GET_INSTANCE = "getInstance";

    private static final String CONSTANT_SCOPED_ACTIVITY_CLASSES = "SCOPED_ACTIVITY_CLASSES";

    public KnightBuilder() throws ProcessorError {
        super(GCN.KNIGHT, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        super.start();
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        addInitAndInstanceMethods();
    }

    private void addInitAndInstanceMethods() {
        // singleton instance
        FieldSpec instance = FieldSpec.builder(getClassName(), "mInstance", Modifier.PRIVATE, Modifier.STATIC).build();
        getBuilder().addField(instance);
        // static method for getting instance
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_GET_INSTANCE)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                        .beginControlFlow("if ($N == null)", instance)
                        .addStatement("throw new $T($S)", IllegalStateException.class, String.format("%s.%s() not called!", getClassName().simpleName(), METHOD_NAME_INIT))
                        .endControlFlow()
                        .addStatement("return $N", instance)
                        .returns(getClassName())
                        .build()
        );
        // initializer
        String app = "application";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_INIT)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(Application.class, app)
                        .addStatement("$N = new $T($N)", instance, getClassName(), app)
                        .addStatement("$N.registerActivityLifecycleCallbacks($N)", app, instance)
                        .build()
        );
    }

    private void addConstructor(ClassName appComponentFactoryName) {
        String app = "application";
        getBuilder().addMethod(
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(Application.class, app)
                        .addStatement("super($T.$N($N))", appComponentFactoryName, ComponentFactoryBuilder.METHOD_NAME_BUILD, app)
                        .build()
        );
    }

    private void addIsScopedMethod(Collection<ActivityBuilders> activityBuildersList) {
        // add constant array of scoped Activity classes
        StringBuilder format = new StringBuilder();
        ClassName[] args = new ClassName[activityBuildersList.size()];

        int i = 0;
        format.append("{\n");
        for (ActivityBuilders activityBuilders : activityBuildersList) {
            format.append("\t$T.class,\n");
            args[i++] = activityBuilders.getActivityName();
        }
        format.append("}");

        getBuilder().addField(
                FieldSpec.builder(Class[].class, CONSTANT_SCOPED_ACTIVITY_CLASSES, Modifier.PRIVATE, Modifier.STATIC)
                        .initializer(format.toString(), args)
                        .build()
        );
        // add isScoped() method
        String cls = "cls";
        String activityClass = "activityClass";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_IS_SCOPED)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PROTECTED)
                        .addParameter(Class.class, activityClass)
                        .returns(boolean.class)
                        .beginControlFlow("for ($T $N : $N)", Class.class, cls, CONSTANT_SCOPED_ACTIVITY_CLASSES)
                        .beginControlFlow("if ($N == $N)", cls, activityClass)
                        .addStatement("return true")
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement("return false")
                        .build()
        );
    }

    private void addOnActivityCreatedMethod(Collection<ActivityBuilders> activityBuildersList) {
        String activity = "activity";
        String stateManager = "stateManager";
        String sc = "sc";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_ON_BUILD_AND_INJECT)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(Activity.class, activity)
                .addParameter(StateManager.class, stateManager)
                .addParameter(IScreenComponent.class, sc)
                .returns(ParameterizedTypeName.get(Pair.class, IScreenComponent.class, IActivityComponent.class));

        boolean first = true;
        for (ActivityBuilders activityBuilders : activityBuildersList) {
            String s = (first) ? "if" : "else if";
            method.beginControlFlow(s + " ($N instanceof $T)", activity, activityBuilders.getActivityName())
                    .addStatement("return $T.$N($N(), $N, ($T) $N, $N)",
                            activityBuilders.CF.getClassName(), ComponentFactoryBuilder.METHOD_NAME_BUILD_AND_INJECT,
                            METHOD_NAME_GET_APPC,
                            sc,
                            activityBuilders.getActivityName(), activity,
                            stateManager)
                    .endControlFlow();
            first = false;
        }

        method.addStatement("return null");

        getBuilder().addMethod(method.build());
    }

    private void addFromMethod(ActivityBuilders activityBuilders) {
        String activity = "activity";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_FROM)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(activityBuilders.getActivityName(), activity)
                        .returns(activityBuilders.AC.getClassName())
                        .addStatement("return ($T) $N().$N($N)",
                                activityBuilders.AC.getClassName(),
                                METHOD_NAME_GET_INSTANCE,
                                METHOD_NAME_GET_AC,
                                activity)
                        .build()
        );
    }

    private void addFromAppMethod(ClassName appComponent) {
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_FROM_APP)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(appComponent)
                        .addStatement("return $N().$N()",
                                METHOD_NAME_GET_INSTANCE, METHOD_NAME_GET_APPC)
                        .build()
        );
    }

    public void setupAppComponent(ClassName appComponent, ClassName appComponentFactory) {
        // set superclass (needs generic ApplicationComponent class)
        getBuilder().superclass(ParameterizedTypeName.get(ClassName.get(ComponentStorage.class), appComponent));
        // add constructor
        addConstructor(appComponentFactory);
        // add fromApp() method
        addFromAppMethod(appComponent);
    }

    public void setupActivities(Collection<ActivityBuilders> activityBuildersList) {
        // for each scoped Activity add from() method
        for (ActivityBuilders activityBuilders : activityBuildersList) {
            addFromMethod(activityBuilders);
        }
        // implement abstract methods from superclass
        addIsScopedMethod(activityBuildersList);
        addOnActivityCreatedMethod(activityBuildersList);
    }
}
