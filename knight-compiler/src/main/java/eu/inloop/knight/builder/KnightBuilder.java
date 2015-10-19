package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.core.IActivityComponent;
import eu.inloop.knight.core.IScreenComponent;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;

/**
 * Class {@link KnightBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-15
 */
public class KnightBuilder extends BaseClassBuilder {

    /**
     * All have parameters: (Activity activity)
     */
    private static final String[] EMPTY_IMPLEMENTATIONS = new String[]{
            "onActivityPaused",
            "onActivityResumed",
            "onActivityStarted",
            "onActivityStopped",
    };
    /**
     * Parameters: (Activity activity, Bundle bundle)
     */
    private static final String METHOD_NAME_ON_CREATED = "onActivityCreated";
    /**
     * Parameters: (Activity activity)
     */
    private static final String METHOD_NAME_ON_DESTROYED = "onActivityDestroyed";
    /**
     * Parameters: (Activity activity, Bundle outState)
     */
    private static final String METHOD_NAME_ON_SAVED = "onActivitySaveInstanceState";

    private static final String METHOD_NAME_INIT = "braceYourselfFor";
    private static final String METHOD_NAME_FROM = "from";
    private static final String METHOD_NAME_FROM_APP = "fromApp";

    private static final String METHOD_NAME_GET_INSTANCE = "getInstance";
    private static final String METHOD_NAME_IS_SCOPED = "isScoped";

    private static final String FIELD_NAME_APPC = "mApplicationComponent";
    private static final String FIELD_NAME_SC_STORAGE = "mScreenComponents";
    private static final String FIELD_NAME_AC_STORAGE = "mActivityComponents";
    private static final String METHOD_NAME_GET_AC = "getActivityComponent";
    private static final String METHOD_NAME_GET_SC = "getScreenComponent";

    private static final String CONSTANT_ACTIVITY_ID = "EXTRA_ACTIVITY_ID";
    private static final String CONSTANT_SCOPED_ACTIVITY_CLASSES = "SCOPED_ACTIVITY_CLASSES";

    public KnightBuilder() throws ProcessorError {
        super(GCN.KNIGHT, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addSuperinterface(EClass.ActivityLifecycleCallbacks.getName());
        addConstants();
        addInitMethod();
        addEmptyMethods();
    }

    private void addConstants() {
        getBuilder().addField(
                FieldSpec.builder(String.class, CONSTANT_ACTIVITY_ID, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", String.format("%s.ACTIVITY_ID", getClassName()))
                        .build()
        );
    }

    private void addInitMethod() {
        FieldSpec instance = FieldSpec.builder(getClassName(), "mInstance", Modifier.PRIVATE, Modifier.STATIC).build();
        getBuilder().addField(instance);

        String app = "application";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_INIT)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(EClass.Application.getName(), app)
                        .addStatement("$N = new $T($N)", instance, getClassName(), app)
                        .addStatement("$N.registerActivityLifecycleCallbacks($N)", app, instance)
                        .build()
        );

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
    }

    private void addConstructor(ClassName appComponentFactoryName) {
        String app = "application";
        getBuilder().addMethod(
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(EClass.Application.getName(), app)
                        .addStatement("$N = $T.$N($N)", FIELD_NAME_APPC, appComponentFactoryName, ComponentFactoryBuilder.METHOD_NAME_BUILD_APPC, app)
                        .build()
        );
    }

    private void addComponentStorageFields(ClassName appComponentName) {
        // Application Component
        getBuilder().addField(
                FieldSpec.builder(appComponentName, FIELD_NAME_APPC, Modifier.PRIVATE).build()
        );
        // Screen Component map
        getBuilder().addField(
                FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, IScreenComponent.class), FIELD_NAME_SC_STORAGE, Modifier.PRIVATE)
                        .initializer("new $T<>()", HashMap.class)
                        .build()
        );
        // Activity Component map
        getBuilder().addField(
                FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, IActivityComponent.class), FIELD_NAME_AC_STORAGE, Modifier.PRIVATE)
                        .initializer("new $T<>()", HashMap.class)
                        .build()
        );
    }

    private void addEmptyMethods() {
        for (String methodName : EMPTY_IMPLEMENTATIONS) {
            getBuilder().addMethod(
                    MethodSpec.methodBuilder(methodName)
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(EClass.Activity.getName(), "activity")
                            .addCode("// do nothing\n")
                            .build()
            );
        }
    }

    private void addOnActivityCreatedMethod(Collection<ActivityBuilders> activityBuildersList) { // TODO
        String activity = "activity";
        String bundle = "bundle";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_ON_CREATED)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(EClass.Activity.getName(), activity)
                .addParameter(EClass.Bundle.getName(), bundle)
                        // execute only for scoped Activities
                .beginControlFlow("if (!$N($N.getClass()))", METHOD_NAME_IS_SCOPED, activity)
                .addStatement("return")
                .endControlFlow()
                .addCode("// TODO\n");

        String activityId = "activityId";
        String sc = "screenComponent";
        String ac = "activityComponent";
        method.addStatement("$T $N", IActivityComponent.class, ac);
        method.addStatement("$T $N = $N.getString($N)", String.class, activityId, bundle, CONSTANT_ACTIVITY_ID);

        method.beginControlFlow("if ($N == null)", activityId)
                .addStatement("$N = $T.randomUUID().toString()", activityId, UUID.class)
                .endControlFlow();
        method.addStatement("$T $N = $N.get($N)", IScreenComponent.class, sc, FIELD_NAME_SC_STORAGE, activityId);
        for (ActivityBuilders activityBuilders : activityBuildersList) {
            method.beginControlFlow("if ($N instanceof $T)", activity, activityBuilders.getActivityName())
                    .endControlFlow();
        }


        getBuilder().addMethod(method.build());
    }

    private void addOnActivitySavedMethod() { // TODO
        String activity = "activity";
        String outState = "outState";

        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_ON_SAVED)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(EClass.Activity.getName(), activity)
                        .addParameter(EClass.Bundle.getName(), outState)
                                // execute only for scoped Activities
                        .beginControlFlow("if (!$N($N.getClass()))", METHOD_NAME_IS_SCOPED, activity)
                        .addStatement("return")
                        .endControlFlow()
                        .addCode("// TODO\n")
                        .build()
        );
    }

    private void addOnActivityDestroyedMethod() { // TODO
        String activity = "activity";

        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_ON_DESTROYED)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(EClass.Activity.getName(), activity)
                                // execute only for scoped Activities
                        .beginControlFlow("if (!$N($N.getClass()))", METHOD_NAME_IS_SCOPED, activity)
                        .addStatement("return")
                        .endControlFlow()
                        .addCode("// TODO\n")
                        .build()
        );
    }

    private void addFromMethod(ActivityBuilders activityBuilders) { // TODO
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_FROM)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .addParameter(activityBuilders.getActivityName(), ProcessorUtils.getParamName(activityBuilders.getActivityName()))
                        .returns(activityBuilders.AC.getClassName())
                        .addStatement("// TODO")
                        .addStatement("return null")
                        .build()
        );
    }

    private void addFromAppMethod(ClassName appComponent) {
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_FROM_APP)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .returns(appComponent)
                        .addStatement("return $N().$N", METHOD_NAME_GET_INSTANCE, FIELD_NAME_APPC)
                        .build()
        );
    }

    public void setupAppComponent(ClassName appComponent, ClassName appComponentFactory) {
        addConstructor(appComponentFactory);
        addComponentStorageFields(appComponent);
        addFromAppMethod(appComponent);
    }

    public void setupActivities(Collection<ActivityBuilders> activityBuildersList) {
        addListOfScopedActivityClasses(activityBuildersList);
        addOnActivityCreatedMethod(activityBuildersList);
        addOnActivitySavedMethod();
        addOnActivityDestroyedMethod();

        for (ActivityBuilders activityBuilders : activityBuildersList) {
            addFromMethod(activityBuilders);
        }
    }

    private void addListOfScopedActivityClasses(Collection<ActivityBuilders> activityBuildersList) {
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

        // add check method
        String cls = "cls";
        String activityClass = "activityClass";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_IS_SCOPED)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
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
}
