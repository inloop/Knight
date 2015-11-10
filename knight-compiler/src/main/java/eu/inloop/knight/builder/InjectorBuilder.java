package eu.inloop.knight.builder;

import android.app.Application;
import android.content.Context;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

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

    public void addInitMethod(KnightBuilder knightBuilder) {
        String app = "app";
        getBuilder().addMethod(
                MethodSpec.methodBuilder(METHOD_NAME_INIT)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(Application.class, app)
                        .addStatement("$T.$N($N)", knightBuilder.getClassName(), KnightBuilder.METHOD_NAME_INIT, app)
                        .build()
        );
    }

    public void addInjectMethod(KnightBuilder knight, ClassName className, List<ClassName> activities) {
        String activity = "activity";
        String object = StringUtils.startLowerCase(className.simpleName());
        MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME_INJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(className, object);

        if (activities.isEmpty()) {
            method.addStatement("$T.$N().$N($N)",
                    knight.getClassName(), KnightBuilder.METHOD_NAME_FROM_APP,
                    BaseComponentBuilder.METHOD_NAME_INJECT, object);
        } else {
            method.addParameter(Context.class, activity);

            for (int i = 0; i < activities.size(); i++) {
                ClassName activityName = activities.get(i);
                if (i > 0) method.addCode("else ");
                method.beginControlFlow("if ($N instanceof $T)", activity, activityName)
                        .addStatement("$T.$N(($T) $N).$N($N)",
                                knight.getClassName(), KnightBuilder.METHOD_NAME_FROM,
                                activityName, activity,
                                BaseComponentBuilder.METHOD_NAME_INJECT,  object)
                        .endControlFlow();
            }
        }

        getBuilder().addMethod(method.build());

    }
}
