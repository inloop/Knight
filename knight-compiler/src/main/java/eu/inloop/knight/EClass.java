package eu.inloop.knight;

import com.squareup.javapoet.ClassName;

import eu.inloop.knight.builder.GPN;

/**
 * Class {@link EClass}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-26
 */
public enum EClass {

    // Android classes
    Context(ClassName.get("android.content", "Context")),
    Application(ClassName.get("android.app", "Application")),
    Activity(ClassName.get("android.app", "Activity")),
    AppCompatActivity(ClassName.get("android.support.v7.app", "AppCompatActivity")),
    Fragment(ClassName.get("android.app", "Fragment")),
    SupportFragment(ClassName.get("android.support.v4.app", "Fragment")),
    View(ClassName.get("android.view", "View")),
    Service(ClassName.get("android.app", "Service")),
    Log(ClassName.get("android.util", "Log")),
    Bundle(ClassName.get("android.os", "Bundle")),
    ActivityLifecycleCallbacks(ClassName.get("android.app.Application", "ActivityLifecycleCallbacks")),

    // Dagger classes
    DaggerApplicationComponent(ClassName.get(GPN.toString(GPN.KNIGHT, GPN.DI, GPN.COMPONENTS), "DaggerApplicationComponent")),
    ;

    private ClassName mClassName;

    EClass(ClassName className) {
        mClassName = className;
    }

    public ClassName getName() {
        return mClassName;
    }

}
