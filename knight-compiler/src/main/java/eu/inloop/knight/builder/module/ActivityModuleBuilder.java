package eu.inloop.knight.builder.module;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import dagger.Provides;
import eu.inloop.knight.ActivityProvided;
import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityModuleBuilder}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class ActivityModuleBuilder extends BaseModuleBuilder {

    public ActivityModuleBuilder(ClassName className) throws ProcessorError {
        super(ActivityScope.class, GCN.ACTIVITY_MODULE, className);
    }

    @Override
    protected void addScopeSpecificPart() {
        // Activity attribute
        FieldSpec activityField = FieldSpec.builder(EClass.Activity.getName(), "mActivity",
                Modifier.PRIVATE, Modifier.FINAL).build();
        getBuilder().addField(activityField);
        // constructor
        String activity = "activity";
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(EClass.Activity.getName(), activity)
                .addStatement("$N = $N", activityField, activity)
                .build();
        getBuilder().addMethod(constructor);
        // provides method for Activity
        String name = "providesActivity";
        mProvidesMethodNames.put(name, 1);
        MethodSpec providesActivity = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Provides.class)
                .addStatement("return $N", activityField)
                .returns(EClass.Activity.getName())
                .build();
        getBuilder().addMethod(providesActivity);
    }

    @Override
    protected Attr getScopeSpecificAnnotationAttributes(Element e) {
        Attr attr = new Attr();
        ActivityProvided a = e.getAnnotation(ActivityProvided.class);
        attr.scoped = a.scoped();
        attr.name = a.named();
        return attr;
    }

}
