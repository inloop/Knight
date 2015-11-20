package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

/**
 * Class {@link ApplicationComponentTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-19
 */
public class ApplicationComponentTest extends BaseTest {

    @Test
    public void noActivity() {
        JavaFileObject navigator = JavaFileObjects.forSourceString("the.knight.di.component.ApplicationComponent",
                Joiner.on('\n').join(
                        "package the.knight.di.component;",
                        "",
                        "import com.example.TestApp;",
                        "import dagger.Component;",
                        "import eu.inloop.knight.core.IAppComponent;",
                        "import eu.inloop.knight.scope.AppScope;",
                        "import javax.annotation.Generated;",
                        "import the.knight.di.module.ApplicationModule;",
                        "",
                        "@Generated(\"eu.inloop.knight.KnightProcessor\")",
                        "@AppScope",
                        "@Component(",
                        "   modules = {",
                        "       ApplicationModule.class,",
                        "   }",
                        ")",
                        "public interface ApplicationComponent extends IAppComponent {",
                        "",
                        "   void inject(TestApp testApp);",
                        "",
                        "}"
                )
        );

        assertFiles(emptyKnightApp())
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivity() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Activity;",
                        "import eu.inloop.knight.KnightActivity;",
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString("the.knight.di.component.ApplicationComponent",
                Joiner.on('\n').join(
                        "package the.knight.di.component;",
                        "",
                        "import com.example.TestApp;",
                        "import dagger.Component;",
                        "import eu.inloop.knight.core.IAppComponent;",
                        "import eu.inloop.knight.scope.AppScope;",
                        "import javax.annotation.Generated;",
                        "import the.knight.di.module.ApplicationModule;",
                        "import the.knight.di.module.ScreenModuleForMainActivity;",
                        "",
                        "@Generated(\"eu.inloop.knight.KnightProcessor\")",
                        "@AppScope",
                        "@Component(",
                        "   modules = {",
                        "       ApplicationModule.class,",
                        "   }",
                        ")",
                        "public interface ApplicationComponent extends IAppComponent {",
                        "",
                        "   void inject(TestApp testApp);",
                        "",
                        "   ScreenComponentForMainActivity plus(ScreenModuleForMainActivity module1);",
                        "",
                        "}"
                )
        );

        assertFiles(emptyKnightApp(), activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivityWithExtraModule() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Activity;",
                        "import eu.inloop.knight.KnightActivity;",
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "}"
                )
        );
        JavaFileObject module = JavaFileObjects.forSourceString("com.example.ScreenModule",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import dagger.Module;",
                        "import eu.inloop.knight.ScreenProvided;",
                        "",
                        "@ScreenProvided(MainActivity.class)",
                        "@Module",
                        "public class ScreenModule {",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString("the.knight.di.component.ApplicationComponent",
                Joiner.on('\n').join(
                        "package the.knight.di.component;",
                        "",
                        "import com.example.ScreenModule;",
                        "import com.example.TestApp;",
                        "import dagger.Component;",
                        "import eu.inloop.knight.core.IAppComponent;",
                        "import eu.inloop.knight.scope.AppScope;",
                        "import javax.annotation.Generated;",
                        "import the.knight.di.module.ApplicationModule;",
                        "import the.knight.di.module.ScreenModuleForMainActivity;",
                        "",
                        "@Generated(\"eu.inloop.knight.KnightProcessor\")",
                        "@AppScope",
                        "@Component(",
                        "   modules = {",
                        "       ApplicationModule.class,",
                        "   }",
                        ")",
                        "public interface ApplicationComponent extends IAppComponent {",
                        "",
                        "   void inject(TestApp testApp);",
                        "",
                        "   ScreenComponentForMainActivity plus(ScreenModuleForMainActivity module1, ScreenModule module2);",
                        "",
                        "}"
                )
        );

        assertFiles(emptyKnightApp(), activity, module)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

}
