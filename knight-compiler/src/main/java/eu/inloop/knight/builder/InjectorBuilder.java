package eu.inloop.knight.builder;

import android.content.Context;
import android.util.Log;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

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

    public InjectorBuilder() throws ProcessorError {
        super(GCN.INJECTOR, GPN.INJECTOR);
        addLogMethod();
    }

    @Override
    public void start() throws ProcessorError {
        super.start();
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    public void addLogMethod() {
        getBuilder().addMethod(
                MethodSpec.methodBuilder("log")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addStatement("$T.d($S, $S)", Log.class, "Logger", "Hello, I'm Injected :)")
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
            method.addStatement("$T.fromApp().inject($N)", knight.getClassName(), object);
        } else {
            method.addParameter(Context.class, activity);

            for (int i = 0; i < activities.size(); i++) {
                ClassName activityName = activities.get(i);
                if (i > 0) method.addCode("else ");
                method.beginControlFlow("if ($N instanceof $T)", activity, activityName)
                        .addStatement("$T.from(($T) $N).inject($N)", knight.getClassName(), activityName, activity, object)
                        .endControlFlow();
            }
        }

        getBuilder().addMethod(method.build());

    }
}
