package eu.inloop.knight.builder;

import android.content.Context;
import android.content.Intent;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;

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

    public void addMethod(ClassName activityName, With[] withParams) {
        // TODO : check @With names - must be distinct

        String forName = getMethodName(METHOD_NAME_FOR, activityName);
        String startName = getMethodName(METHOD_NAME_START, activityName);

        String context = "context";
        String intent = "intent";

        MethodSpec.Builder forMethod = MethodSpec.methodBuilder(forName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Context.class, context)
                .returns(Intent.class);
        MethodSpec.Builder startMethod = MethodSpec.methodBuilder(startName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Context.class, context);

        forMethod.addStatement("$T $N = new $T($N, $T.class)", Intent.class, intent, Intent.class, context, activityName);
        startMethod.addCode("$N.startActivity($N($N", context, forName, context);
        for (With with : withParams) {
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

        getBuilder().addMethod(forMethod.build());
        getBuilder().addMethod(startMethod.build());
    }

    private String getMethodName(String format, ClassName activityName) {
        return String.format(format, activityName.simpleName());
    }

    private String getExtraId(ClassName activityName, String name) {
        return String.format(EXTRA_ID, activityName.simpleName(), name);
    }
}
