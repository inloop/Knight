package eu.inloop.knight.builder;

import android.content.Context;
import android.content.Intent;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.Extra;
import eu.inloop.knight.name.GCN;
import eu.inloop.knight.name.GPN;
import eu.inloop.knight.util.ProcessorError;

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
        super.start();
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC);
    }

    public void integrate(TypeElement e, ActivityBuilders activityBuilders) throws ProcessorError {
        // get all fields annotated with @Extra
        List<NamedExtra> extras = new ArrayList<>();
        for (Element ee : e.getEnclosedElements()) {
            if (ee.getAnnotation(Extra.class) != null) {
                extras.add(new NamedExtra((VariableElement) ee));
            }
        }
        // make sure that @Extra have distinct names
        Set<String> names = new HashSet<>();
        for (NamedExtra namedExtra : extras) {
            if (names.contains(namedExtra.getName())) {
                throw new ProcessorError(namedExtra.getElement(), ErrorMsg.Extra_name_not_unique);
            }
            names.add(namedExtra.getName());
        }

        addForActivityMethod(activityBuilders.getActivityName(), extras);
        activityBuilders.SM.addInitMethod(extras);
    }

    private void addForActivityMethod(ClassName activityName, List<NamedExtra> extras) {
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
        for (NamedExtra namedExtra : extras) {
            TypeName typeName = ClassName.get(namedExtra.getElement().asType());
            String name = namedExtra.getName();
            forMethod.addParameter(typeName, name);
            forMethod.addStatement("$N.putExtra($S, $N)", intent, getExtraId(activityName, name), name);

            startMethod.addParameter(typeName, name);
            startMethod.addCode(", $N", name);
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
