package eu.inloop.knight.builder.module;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import eu.inloop.knight.EClass;
import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ScreenModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class ScreenModuleBuilder extends BaseModuleBuilder {

    private static final String FIELD_NAME_STATE_MANAGER = "mStateManager";
    private static final String STATEFUL_ID_FORMAT = "[%s]%s";

    public ScreenModuleBuilder(ClassName className) throws ProcessorError {
        super(ScreenScope.class, GCN.SCREEN_MODULE, className);
    }

    public ScreenModuleBuilder(GCN genClassName, ClassName className) throws ProcessorError {
        super(ScreenScope.class, genClassName, className);
    }

    @Override
    protected void addScopeSpecificPart() {
        // Application attribute
        FieldSpec stateField = FieldSpec.builder(EClass.StateManager.getName(), FIELD_NAME_STATE_MANAGER,
                Modifier.PRIVATE, Modifier.FINAL).build();
        getBuilder().addField(stateField);
        // constructor
        String stateManager = "stateManager";
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(EClass.StateManager.getName(), stateManager)
                .addStatement("$N = $N", stateField, stateManager)
                .build();
        getBuilder().addMethod(constructor);
    }

    @Override
    protected Attr getScopeSpecificAnnotationAttributes(Element e) {
        Attr attr = new Attr();
        ScreenProvided a = e.getAnnotation(ScreenProvided.class);
        attr.scoped = a.scoped();
        attr.name = a.named();
        return attr;
    }

    // TODO : only if annotated with @ScreenScope
    @Override
    protected void addProvideStatement(MethodSpec.Builder method, ExecutableElement e, String callFormat, Object... args) {
        MethodSpec m = method.build();

        method.addCode("return $N.manage($S, ", FIELD_NAME_STATE_MANAGER, String.format(STATEFUL_ID_FORMAT, m.returnType, m.name));
        addProvideStatement(false, method, e, callFormat, args);
        method.addCode(");\n");
    }

}
