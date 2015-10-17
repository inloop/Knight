package eu.inloop.knight;

import com.squareup.javapoet.ClassName;

/**
 * Class {@link EClass}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-26
 */
public enum EClass {

    Context(ClassName.get("android.content", "Context")),
    Application(ClassName.get("android.app", "Application")),
    Activity(ClassName.get("android.app", "Activity")),
    AppCompatActivity(ClassName.get("android.support.v7.app", "AppCompatActivity")),
    Log(ClassName.get("android.util", "Log")),
    ;

    private ClassName mClassName;

    EClass(ClassName className) {
        mClassName = className;
    }

    public ClassName getName() {
        return mClassName;
    }

}
