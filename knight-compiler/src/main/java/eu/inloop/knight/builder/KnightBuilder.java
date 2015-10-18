package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.ProcessorUtils;

/**
 * Class {@link KnightBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-15
 */
public class KnightBuilder extends BaseClassBuilder {

    private static final String METHOD_NAME_FROM = "from";
    private static final String METHOD_NAME_FROM_APP = "fromApp";

    public KnightBuilder() throws ProcessorError {
        super(GCN.KNIGHT, GPN.KNIGHT);
    }

    @Override
    public void start() throws ProcessorError {
        getBuilder().addModifiers(Modifier.FINAL, Modifier.PUBLIC);
    }

    @Override
    public void end() throws ProcessorError {

    }

    public void addFromMethod(ActivityBuilders activityBuilders) {
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
