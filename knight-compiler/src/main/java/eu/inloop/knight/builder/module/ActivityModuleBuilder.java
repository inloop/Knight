package eu.inloop.knight.builder.module;

import android.app.Activity;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

import dagger.Provides;
import eu.inloop.knight.name.GCN;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityModuleBuilder} is used for creating Activity Module class file.
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class ActivityModuleBuilder extends BaseModuleBuilder {

    private static final String FIELD_NAME_ACTIVITY = "mActivity";

    /**
     * Constructor
     *
     * @param className Class name of Activity for which this module will be generated.
     */
    public ActivityModuleBuilder(ClassName className) throws ProcessorError {
        super(ActivityScope.class, GCN.ACTIVITY_MODULE, className);
    }

    @Override
    protected void addScopeSpecificPart() {
        // Activity attribute
        FieldSpec activityField = FieldSpec.builder(Activity.class, FIELD_NAME_ACTIVITY,
                Modifier.PRIVATE, Modifier.FINAL).build();
        getBuilder().addField(activityField);
        // constructor
        String activity = "activity";
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Activity.class, activity)
                .addStatement("$N = $N", activityField, activity)
                .build();
        getBuilder().addMethod(constructor);
        // provides method for Activity
        MethodSpec providesActivity = MethodSpec.methodBuilder(createProvideMethodName(activity))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Provides.class)
                .addStatement("return $N", activityField)
                .returns(Activity.class)
                .build();
        getBuilder().addMethod(providesActivity);
    }

}
