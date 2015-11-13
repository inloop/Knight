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

    // Annotation classes
    Override(ClassName.get("java.lang", "Override")),
    AppProvided(ClassName.get("eu.inloop.knight", "AppProvided")),
    ScreenProvided(ClassName.get("eu.inloop.knight", "ScreenProvided")),
    ActivityProvided(ClassName.get("eu.inloop.knight", "ActivityProvided")),
    AppScope(ClassName.get("eu.inloop.knight.scope", "AppScope")),
    ScreenScope(ClassName.get("eu.inloop.knight.scope", "ScreenScope")),
    ActivityScope(ClassName.get("eu.inloop.knight.scope", "ActivityScope")),
    Nullable(ClassName.get("android.support.annotation", "Nullable")),

    // Android classes
    Context(ClassName.get("android.content", "Context")),
    Application(ClassName.get("android.app", "Application")),
    Activity(ClassName.get("android.app", "Activity")),
    AppCompatActivity(ClassName.get("android.support.v7.app", "AppCompatActivity")),
    Fragment(ClassName.get("android.app", "Fragment")),
    SupportFragment(ClassName.get("android.support.v4.app", "Fragment")),
    View(ClassName.get("android.view", "View")),
    Log(ClassName.get("android.util", "Log")),
    Bundle(ClassName.get("android.os", "Bundle")),
    ActivityLifecycleCallbacks(ClassName.get("android.app.Application", "ActivityLifecycleCallbacks")),

    // Knight classes
    ComponentStorage(ClassName.get("eu.inloop.knight", "ComponentStorage")),
    StateManager(ClassName.get("eu.inloop.knight", "StateManager")),

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
