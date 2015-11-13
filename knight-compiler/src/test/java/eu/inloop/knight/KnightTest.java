package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;

/**
 * Class {@link KnightTest}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-20
 */
public class KnightTest {

    private JavaFileObject componentStorage() {
        return JavaFileObjects.forSourceString("eu.inloop.knight.ComponentStorage",
                Joiner.on('\n').join(
                        "package eu.inloop.knight;",
                        "",
                        "import android.app.Application.ActivityLifecycleCallbacks;",
                        "import android.app.Activity;",
                        "import android.os.Bundle;",
                        "import android.util.Pair;",
                        "",
                        "import eu.inloop.knight.core.IActivityComponent;",
                        "import eu.inloop.knight.core.IAppComponent;",
                        "import eu.inloop.knight.core.IScreenComponent;",
                        "",
                        "public abstract class ComponentStorage<T> implements ActivityLifecycleCallbacks {",
                        "",
                        "   public ComponentStorage(T t) {}",
                        "",
                        "   protected final T getApplicationComponent() {",
                        "        return null;",
                        "    }",
                        "",
                        "    protected final IActivityComponent getActivityComponent(Activity activity) {",
                        "        return null;",
                        "    }",
                        "",
                        "    protected abstract boolean isScoped(Class activityClass);",
                        "",
                        "    protected abstract Pair<IScreenComponent, IActivityComponent> buildComponentsAndInject(Activity activity, StateManager manager, IScreenComponent sc);",
                        "",
                        "    @Override",
                        "    public final void onActivityCreated(Activity activity, Bundle bundle) {",
                        "        // do nothing",
                        "    }",
                        "",
                        "    @Override",
                        "    public final void onActivityDestroyed(Activity activity) {",
                        "        // do nothing",
                        "    }",
                        "",
                        "    @Override",
                        "    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {",
                        "        // do nothing",
                        "    }",
                        "",
                        "    @Override",
                        "    public final void onActivityStarted(Activity activity) {",
                        "        // do nothing",
                        "    }",
                        "",
                        "    @Override",
                        "    public final void onActivityResumed(Activity activity) {",
                        "        // do nothing",
                        "    }",
                        "",
                        "    @Override",
                        "    public final void onActivityPaused(Activity activity) {",
                        "        // do nothing",
                        "    }",
                        "",
                        "    @Override",
                        "    public final void onActivityStopped(Activity activity) {",
                        "        // do nothing",
                        "    }",
                        "}"
                )
        );
    }

    private JavaFileObject libClass(String className) {
        return JavaFileObjects.forSourceString(String.format("eu.inloop.knight.%s", className),
                Joiner.on('\n').join(
                        "package eu.inloop.knight;",
                        "",
                        String.format("public class %s {}", className)
                )
        );
    }

    private JavaFileObject exampleActivity() {
        return JavaFileObjects.forSourceString("com.example.ExampleActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Activity;",
                        "import eu.inloop.knight.KnightActivity;",
                        "import eu.inloop.knight.With;",
                        "",
                        "@KnightActivity({",
                        "       @With(name = \"a\", type = String.class)",
                        "})",
                        "public class ExampleActivity extends Activity {",
                        "}"
                )
        );
    }

    private Iterable<JavaFileObject> files(JavaFileObject... f) {
        List<JavaFileObject> files = new ArrayList<>();
        files.add(componentStorage());
        files.add(libClass("StateManager"));
        files.add(exampleActivity());
        files.addAll(Arrays.asList(f));
        return files;
    }

    //@Test
    public void knightClass() {
        JavaFileObject knightFile = JavaFileObjects.forSourceString("the.knight.Knight",
                Joiner.on('\n').join(
                        "package the.knight;",
                        "",
                        "public final class Knight {",
                        "",
                        "}"
                )
        );
        JavaFileObject appComponentFile = JavaFileObjects.forSourceString("the.knight.di.module",
                Joiner.on('\n').join(
                        "package the.knight.di.component;",
                        "",
                        "public final class ApplicationComponent {",
                        "",
                        "}"
                )
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files())
                .processedWith(new KnightProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(knightFile)
                .and()
                .generatesSources(appComponentFile);
    }

}
