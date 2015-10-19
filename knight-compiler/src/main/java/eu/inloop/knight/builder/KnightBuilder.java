package eu.inloop.knight.builder;

import android.util.Pair;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Collection;

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
     * Overridden method with parameters: (Activity activity, Bundle bundle, String uuid)
     */
    private static final String METHOD_NAME_ON_ACTIVITY_CREATED = "onActivityCreated";
    /**
     * Overridden method with parameters: (Activity activity, String uuid)
     */
    private static final String METHOD_NAME_ON_ACTIVITY_DESTROYED = "onActivityDestroyed";
    /**
     * Overridden method with parameters: (Activity activity, Bundle outState, String uuid)
     */
    private static final String METHOD_NAME_ON_ACTIVITY_SAVED = "onActivitySaved";
    /**
     * Overridden method with parameters: (Activity activity)
     */
    private static final String METHOD_NAME_IS_SCOPED = "isScoped";

    private static final String METHOD_NAME_GET_APPC = "getApplicationComponent";
    private static final String METHOD_NAME_GET_AC = "getActivityComponent";
    private static final String METHOD_NAME_GET_SC = "getScreenComponent";
    private static final String METHOD_NAME_PUT_SC = "putScreenComponent";
    private static final String METHOD_NAME_PUT_AC = "putActivityComponent";
    private static final String METHOD_NAME_REMOVE_SC = "removeScreenComponent";
    private static final String METHOD_NAME_REMOVE_AC = "removeActivityComponent";

    private static final String METHOD_NAME_INIT = "braceYourselfFor";
    private static final String METHOD_NAME_FROM = "from";
    private static final String METHOD_NAME_FROM_APP = "fromApp";

    private static final String METHOD_NAME_GET_INSTANCE = "getInstance";

    private static final String CONSTANT_SCOPED_ACTIVITY_CLASSES = "SCOPED_ACTIVITY_CLASSES";

    public KnightBuilder() throws ProcessorError {
        super(GCN.KNIGHT, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        addInitMethod();
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
                        .addStatement("super($T.$N($N))", appComponentFactoryName, ComponentFactoryBuilder.METHOD_NAME_BUILD, app)
                        .build()
        );
    }

    private void addOnActivityCreatedMethod(Collection<ActivityBuilders> activityBuildersList) { // TODO
        String activity = "activity";
        String bundle = "bundle";
        String uuid = "uuid";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_ON_ACTIVITY_CREATED)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(EClass.Activity.getName(), activity)
                .addParameter(EClass.Bundle.getName(), bundle)
                .addParameter(String.class, uuid);

        String sc = "screenComponent";
        String pair = "components";

        method.addStatement("$T<? extends $T, ? extends $T> $N", Pair.class, IScreenComponent.class, IActivityComponent.class, pair);
        method.addStatement("$T $N = $N($N)", IScreenComponent.class, sc, METHOD_NAME_GET_SC, uuid);

        boolean first = true;
        for (ActivityBuilders activityBuilders : activityBuildersList) {
            String s = (first) ? "if" : "else if";
            method.beginControlFlow(s + " ($N instanceof $T)", activity, activityBuilders.getActivityName())
                    .addStatement("$N = $T.$N($N(), $N, $N, ($T) $N)",
                            pair,
                            activityBuilders.CF.getClassName(), ComponentFactoryBuilder.METHOD_NAME_BUILD_AND_INJECT,
                            METHOD_NAME_GET_APPC,
                            sc,
                            bundle,
                            activityBuilders.getActivityName(), activity)
                    .endControlFlow();
            first = false;
        }

        method.beginControlFlow("else")
                .addStatement("return")
                .endControlFlow();
        method.beginControlFlow("if ($N == null)", sc)
                .addStatement("$N($N, $N.first)", METHOD_NAME_PUT_SC, uuid, pair)
                .endControlFlow();
        method.addStatement("$N($N, $N.second)", METHOD_NAME_PUT_AC, uuid, pair);

        getBuilder().addMethod(method.build());
    }

    private void addOnActivitySavedMethod() { // TODO
        String activity = "activity";
        String outState = "outState";
        String uuid = "uuid";

        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_ON_ACTIVITY_SAVED)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PROTECTED)
                        .addParameter(EClass.Activity.getName(), activity)
                        .addParameter(EClass.Bundle.getName(), outState)
                        .addParameter(String.class, uuid)
                        .addCode("// TODO\n")
                        .build()
        );
    }

    private void addOnActivityDestroyedMethod() { // TODO
        String activity = "activity";
        String uuid = "uuid";

        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_ON_ACTIVITY_DESTROYED)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PROTECTED)
                        .addParameter(EClass.Activity.getName(), activity)
                        .addParameter(String.class, uuid)
                        .addCode("// TODO\n")
                        .build()
        );
    }

    private void addFromMethod(ActivityBuilders activityBuilders) { // TODO
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_FROM)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
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
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(appComponent)
                        .addStatement("return $N().$N()", METHOD_NAME_GET_INSTANCE, METHOD_NAME_GET_APPC)
                        .build()
        );
    }

    public void setupAppComponent(ClassName appComponent, ClassName appComponentFactory) {
        getBuilder().superclass(ParameterizedTypeName.get(EClass.ComponentStorage.getName(), appComponent));
        addConstructor(appComponentFactory);
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
}
