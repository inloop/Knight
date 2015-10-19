package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import java.util.Collection;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
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

    public KnightBuilder() throws ProcessorError {
        super(GCN.KNIGHT, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addSuperinterface(EClass.ActivityLifecycleCallbacks.getName());
        addInitMethod();
        addEmptyMethods();
    }

    private void addInitMethod() {
        String instance = "sInstance";
        getBuilder().addField(
                FieldSpec.builder(getClassName(), instance, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T()", getClassName())
                        .build()
        );

        String app = "application";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_INIT)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(EClass.Application.getName(), app)
                        .addStatement("$N.registerActivityLifecycleCallbacks($N)", app, instance)
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

    private void addOnActivityCreatedMethod(Collection<ActivityBuilders> activityBuildersList) {
        String activity = "activity";
        String bundle = "bundle";

        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_ON_CREATED)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(EClass.Activity.getName(), activity)
                .addParameter(EClass.Bundle.getName(), bundle)
                .addCode("// TODO\n");

        for (ActivityBuilders activityBuilders : activityBuildersList) {
            method.beginControlFlow("if ($N instanceof $T)", activity, activityBuilders.getActivityName());
            method.endControlFlow();
        }


        getBuilder().addMethod(method.build());
    }

    private void addOnActivitySavedMethod() {
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_ON_SAVED)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(EClass.Activity.getName(), "activity")
                        .addParameter(EClass.Bundle.getName(), "outState")
                        .addCode("// TODO\n")
                        .build()
        );
    }

    private void addOnActivityDestroyedMethod() {
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_ON_DESTROYED)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(EClass.Activity.getName(), "activity")
                        .addCode("// TODO\n")
                        .build()
        );
    }

    public void addFromMethods(Collection<ActivityBuilders> activityBuildersList) {
        addOnActivityCreatedMethod(activityBuildersList);
        addOnActivitySavedMethod();
        addOnActivityDestroyedMethod();

        // add from methods
        for (ActivityBuilders activityBuilders : activityBuildersList) {
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
    }

    public void addFromAppMethod(ClassName appComponent) {
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_FROM_APP)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .returns(appComponent)
                        .addStatement("// TODO")
                        .addStatement("return null")
                        .build()
        );
    }
}
