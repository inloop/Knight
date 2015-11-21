package eu.inloop.knight.builder.module;

import android.app.Application;
import android.content.res.Resources;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import dagger.Provides;
import eu.inloop.knight.name.GCN;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppModuleBuilder} is used for creating Application Module class file.
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class AppModuleBuilder extends BaseModuleBuilder {

    private static final String FIELD_NAME_APPLICATION = "mApplication";

    /**
     * Constructor
     * @param appClassName
     */
    public AppModuleBuilder(ClassName appClassName) throws ProcessorError {
        super(AppScope.class, GCN.APPLICATION_MODULE, appClassName);
    }

    @Override
    protected void addScopeSpecificPart() {
        // Application attribute
        FieldSpec appField = FieldSpec.builder(getArgClassName(), FIELD_NAME_APPLICATION,
                Modifier.PRIVATE, Modifier.FINAL).build();
        getBuilder().addField(appField);
        // constructor
        String app = "application";
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(getArgClassName(), app)
                .addStatement("$N = $N", appField, app)
                .build();
        getBuilder().addMethod(constructor);
        // provides method for Knight Application
        MethodSpec providesApp = MethodSpec.methodBuilder(createProvideMethodName(getArgClassName().simpleName()))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Provides.class)
                .addStatement("return $N", appField)
                .returns(getArgClassName())
                .build();
        getBuilder().addMethod(providesApp);
        // provides method for Application
        providesApp = MethodSpec.methodBuilder(createProvideMethodName(app))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Provides.class)
                .addStatement("return $N", appField)
                .returns(Application.class)
                .build();
        getBuilder().addMethod(providesApp);
        // provides method for Application Resources
        MethodSpec providesAppResources = MethodSpec.methodBuilder(createProvideMethodName("applicationResources"))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Provides.class)
                .addStatement("return $N.getResources()", appField)
                .returns(Resources.class)
                .build();
        getBuilder().addMethod(providesAppResources);
    }

}
