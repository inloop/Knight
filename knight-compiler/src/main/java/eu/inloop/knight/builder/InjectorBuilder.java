package eu.inloop.knight.builder;

import android.app.Application;
import android.content.Context;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.Injectable;
import eu.inloop.knight.builder.component.BaseComponentBuilder;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link InjectorBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-11-05
 */
public class InjectorBuilder extends BaseClassBuilder {

    public static final String METHOD_NAME_INJECT = "inject";
    public static final String METHOD_NAME_INIT = "init";

    public InjectorBuilder() throws ProcessorError {
        super(GCN.INJECTOR, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        super.start();
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    public void addInitMethod(KnightBuilder knightBuilder, ClassName appClassName) {
        String app = "app";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_INIT)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(appClassName, app)
                        .addStatement("$T.$N($N)", knightBuilder.getClassName(), KnightBuilder.METHOD_NAME_INIT, app)
                        .addStatement("$T.$N().$N($N)", knightBuilder.getClassName(), KnightBuilder.METHOD_NAME_FROM_APP, BaseComponentBuilder.METHOD_NAME_INJECT, app)
                        .build()
        );
    }

    public void addInjectMethod(KnightBuilder knight, Injectable injectable) {
        String context = "context";
        String object = StringUtils.startLowerCase(injectable.getClassName().simpleName());
        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_INJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(injectable.getClassName(), object)
                .addParameter(Context.class, context);

        boolean first = true;
        if (!injectable.getmFromActivities().isEmpty()) {
            for (ClassName activityName : injectable.getmFromActivities()) {
                if (first) {
                    first = false;
                } else {
                    method.addCode("else ");
                }
                method.beginControlFlow("if ($N instanceof $T)", context, activityName)
                        .addStatement("$T.$N(($T) $N).$N($N)",
                                knight.getClassName(), KnightBuilder.METHOD_NAME_FROM,
                                activityName, context,
                                BaseComponentBuilder.METHOD_NAME_INJECT, object)
                        .endControlFlow();
            }
        }
        if (injectable.getmFromApp() != null) {
            if (first) {
                first = false;
            } else {
                method.addCode("else ");
            }

            method.beginControlFlow("if ($N == null || $N instanceof $T)", context, context, Application.class)
                    .addStatement("$T.$N().$N($N)",
                            knight.getClassName(), KnightBuilder.METHOD_NAME_FROM_APP,
                            BaseComponentBuilder.METHOD_NAME_INJECT, object)
                    .endControlFlow();
        }

        getBuilder().addMethod(method.build());

    }
}
