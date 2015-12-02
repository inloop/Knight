package eu.inloop.knight.builder.assisted;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Provider;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import eu.inloop.knight.assisted.Assisted;
import eu.inloop.knight.builder.BaseClassBuilder;
import eu.inloop.knight.name.GCN;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link FactoryBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-11-22
 */
public class FactoryBuilder extends BaseClassBuilder {

    private static final String METHOD_NAME_GETTER = "get";
    private static final String PARAM_NAME_PROVIDER = "%sProvider";
    private final ExecutableElement mElement;
    private final Class<? extends Annotation> mScope;
    private boolean mManaged;

    private List<ParameterSpec> mAssistedParams;
    private List<ParameterSpec> mProvidedParams;

    public FactoryBuilder(ClassName arg, ExecutableElement e, Class<? extends Annotation> scope) throws ProcessorError {
        super(GCN.FACTORY, arg);
        mElement = e;
        mScope = scope;
        mManaged = false;

        mAssistedParams = new ArrayList<>();
        mProvidedParams = new ArrayList<>();
    }

    @Override
    protected String getPackage() {
        return getArgClassName().packageName();
    }

    @Override
    public void start() throws ProcessorError {
        super.start();
        getBuilder().addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public void end() throws ProcessorError {
        super.end();
        addConstructor();
        addGetMethod();
    }

    private void addConstructor() {
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        for (int i = 0; i < mElement.getParameters().size(); i++) {
            VariableElement p = mElement.getParameters().get(i);

            Assisted assisted = p.getAnnotation(Assisted.class);
            if (assisted != null && assisted.id() && mScope == ScreenScope.class) {
                mManaged = true;
            }
            boolean isProvided = (assisted == null);

            Set<Modifier> modifiers = p.getModifiers();

            TypeName type = ClassName.get(p.asType());
            String name = p.getSimpleName().toString();
            if (isProvided) {
                // TODO : check for primitives
                type = ParameterizedTypeName.get(ClassName.get(Provider.class), type);
                name = String.format(PARAM_NAME_PROVIDER, name);

                addAttribute(type, name);
                constructor.addStatement("this.$N = $N", name, name);
            }

            ParameterSpec.Builder paramBuilder = ParameterSpec.builder(
                    type, name, modifiers.toArray(new Modifier[modifiers.size()])
            );
            // add also Qualifier annotations
            for (AnnotationSpec a : getQualifiers(p)) {
                paramBuilder.addAnnotation(a);
            }
            ParameterSpec param = paramBuilder.build();

            if (isProvided) {
                constructor.addParameter(param);
                mProvidedParams.add(param);
            } else {
                mAssistedParams.add(param);
            }
        }

        getBuilder().addMethod(constructor.build());
    }

    private void addGetMethod() {
        MethodSpec.Builder getter = MethodSpec.methodBuilder(METHOD_NAME_GETTER)
                .addModifiers(Modifier.PUBLIC)
                .returns(getArgClassName()); // TODO : support @As

        boolean first = true;
        getter.addCode("return new $T(", getArgClassName());

        for (int i = 0; i < mProvidedParams.size(); i++) {
            if (!first) getter.addCode(", ");
            first = false;

            getter.addCode("$N.get()", mProvidedParams.get(i).name);
        }
        for (int i = 0; i < mAssistedParams.size(); i++) {
            getter.addParameter(mAssistedParams.get(i));

            if (!first) getter.addCode(", ");
            first = false;

            getter.addCode("$N", mAssistedParams.get(i).name);
        }

        getter.addCode(");\n");

        getBuilder().addMethod(getter.build());
    }

    private void addAttribute(TypeName type, String name) {
        getBuilder().addField(
                FieldSpec.builder(type, name, Modifier.PRIVATE, Modifier.FINAL).build()
        );
    }
}
