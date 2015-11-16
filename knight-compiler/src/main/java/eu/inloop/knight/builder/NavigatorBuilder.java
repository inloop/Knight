package eu.inloop.knight.builder;

import android.content.Context;
import android.content.Intent;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.KnightActivity;
import eu.inloop.knight.With;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;

/**
 * Class {@link NavigatorBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-21
 */
public class NavigatorBuilder extends BaseClassBuilder {

    private static final String METHOD_NAME_FOR = "for%s";
    private static final String METHOD_NAME_START = "start%s";
    private static final String EXTRA_ID = "%s.EXTRA_%s";

    public NavigatorBuilder() throws ProcessorError {
        super(GCN.NAVIGATOR, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC);
    }

    public void integrate(TypeElement e, ActivityBuilders activityBuilders) throws ProcessorError {
        With[] withParams = e.getAnnotation(KnightActivity.class).with();

        // make sure that @With have distinct names
        Set<String> names = new HashSet<>();
        for (With with : withParams) {
            names.add(with.name().toLowerCase());
        }
        if (names.size() != withParams.length) {
            throw new ProcessorError(e, ErrorMsg.With_name_not_unique);
        }

        addForActivityMethod(activityBuilders.getActivityName(), withParams);
        activityBuilders.SM.addInitMethod(withParams);
    }

    private void addForActivityMethod(ClassName activityName, With[] params) {
        String forName = getMethodName(METHOD_NAME_FOR, activityName);
        String context = "context";
        String intent = "intent";
        // build FOR method
        MethodSpec.Builder forMethod = MethodSpec.methodBuilder(forName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Context.class, context)
                .returns(Intent.class);
        // build START method
        MethodSpec.Builder startMethod = MethodSpec.methodBuilder(getMethodName(METHOD_NAME_START, activityName))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Context.class, context);

        forMethod.addStatement("$T $N = new $T($N, $T.class)", Intent.class, intent, Intent.class, context, activityName);
        startMethod.addCode("$N.startActivity($N($N", context, forName, context);
        for (With with : params) {
            TypeName typeName = ProcessorUtils.getType(with, new ProcessorUtils.IGetter<With, Class<?>>() {
                @Override
                public Class<?> get(With obj) {
                    return obj.type();
                }
            });
            forMethod.addParameter(typeName, with.name());
            forMethod.addStatement("$N.putExtra($S, $N)", intent, getExtraId(activityName, with.name()), with.name());

            startMethod.addParameter(typeName, with.name());
            startMethod.addCode(", $N", with.name());
        }
        forMethod.addStatement("return $N", intent);
        startMethod.addCode("));\n");
        // add methods
        getBuilder().addMethod(forMethod.build());
        getBuilder().addMethod(startMethod.build());
    }

    private String getMethodName(String format, ClassName activityName) {
        return String.format(format, activityName.simpleName());
    }

    public static String getExtraId(ClassName activityName, String name) {
        return String.format(EXTRA_ID, activityName.simpleName(), name);
    }
}
