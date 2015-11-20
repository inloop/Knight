package eu.inloop.knight;

import android.app.Activity;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.annotation.Generated;
import javax.tools.JavaFileObject;

import dagger.Component;
import dagger.Module;
import eu.inloop.knight.core.IAppComponent;
import eu.inloop.knight.scope.AppScope;

/**
 * Class {@link ApplicationComponentTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-19
 */
public class ApplicationComponentTest extends BaseTest {

    @Test
    public void noActivity() {
        JavaFileObject component = JavaFileObjects.forSourceString(KNIGHT_COMPONENT + "ApplicationComponent",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT_COMPONENT,
                        "",
                        importClass(
                                "com.example.TestApp",
                                KNIGHT_MODULE + "ApplicationModule",
                                Component.class,
                                IAppComponent.class,
                                AppScope.class,
                                Generated.class
                        ),
                        "",
                        GENERATED,
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

        assertFiles(EMPTY_KNIGHT_APP)
                .compilesWithoutError()
                .and()
                .generatesSources(component);
    }

    @Test
    public void oneActivity() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "}"
                )
        );

        JavaFileObject component = JavaFileObjects.forSourceString(KNIGHT_COMPONENT + "ApplicationComponent",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT_COMPONENT,
                        "",
                        importClass(
                                "com.example.TestApp",
                                KNIGHT_MODULE + "ApplicationModule",
                                KNIGHT_MODULE + "ScreenModuleForMainActivity",
                                Component.class,
                                IAppComponent.class,
                                AppScope.class,
                                Generated.class
                        ),
                        "",
                        GENERATED,
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

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(component);
    }

    @Test
    public void oneActivityWithExtraModule() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class
                        ),
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
                        importClass(
                                Module.class,
                                ScreenProvided.class
                        ),
                        "",
                        "@ScreenProvided(MainActivity.class)",
                        "@Module",
                        "public class ScreenModule {",
                        "}"
                )
        );

        JavaFileObject component = JavaFileObjects.forSourceString(KNIGHT_COMPONENT + "ApplicationComponent",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT_COMPONENT,
                        "",
                        importClass(
                                "com.example.ScreenModule",
                                "com.example.TestApp",
                                KNIGHT_MODULE + "ApplicationModule",
                                KNIGHT_MODULE + "ScreenModuleForMainActivity",
                                Component.class,
                                IAppComponent.class,
                                AppScope.class,
                                Generated.class
                        ),
                        "",
                        GENERATED,
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

        assertFiles(EMPTY_KNIGHT_APP, activity, module)
                .compilesWithoutError()
                .and()
                .generatesSources(component);
    }

}
