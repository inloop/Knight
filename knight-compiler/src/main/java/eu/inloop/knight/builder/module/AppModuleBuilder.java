package eu.inloop.knight.builder.module;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import dagger.Provides;
import eu.inloop.knight.AppProvided;
import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class AppModuleBuilder extends ModuleBuilder {

    public AppModuleBuilder() throws ProcessorError {
        super(AppScope.class, GCN.APPLICATION_MODULE);
    }

    @Override
    protected void addScopeSpecificPart() {
        // Application attribute
        FieldSpec appField = FieldSpec.builder(EClass.Application.getName(), "mApplication",
                Modifier.PRIVATE, Modifier.FINAL).build();
        getBuilder().addField(appField);
        // constructor
        String app = "application";
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(EClass.Application.getName(), app)
                .addStatement("$N = $N", appField, app)
                .build();
        getBuilder().addMethod(constructor);
        // provides method for Application
        String name = "providesApplication";
        mProvidesMethodNames.put(name, 1);
        MethodSpec providesApp = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Provides.class)
                .addStatement("return $N", appField)
                .returns(EClass.Application.getName())
                .build();
        getBuilder().addMethod(providesApp);
    }

    @Override
    protected Attr getScopeSpecificAnnotationAttributes(Element e) {
        Attr attr = new Attr();
        AppProvided a = e.getAnnotation(AppProvided.class);
        attr.scoped = a.scoped();
        attr.name = a.named();
        return attr;
    }

}
