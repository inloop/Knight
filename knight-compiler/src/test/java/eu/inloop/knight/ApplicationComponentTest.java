package eu.inloop.knight;

import android.app.Activity;

import org.junit.Test;

import javax.tools.JavaFileObject;

import dagger.Component;
import dagger.Module;
import eu.inloop.knight.core.IAppComponent;
import eu.inloop.knight.scope.AppScope;

import static eu.inloop.knight.util.File.file;
import static eu.inloop.knight.util.File.generatedFile;

/**
 * Class {@link ApplicationComponentTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-19
 */
public class ApplicationComponentTest extends BaseTest {

    @Test
    public void noActivity() {
        JavaFileObject component = generatedFile(P_KNIGHT_COMPONENT, "ApplicationComponent")
                .imports(
                        EMPTY_KNIGHT_APP,
                        P_KNIGHT_MODULE + ".ApplicationModule", "AM",
                        AppScope.class, "AS",
                        Component.class,
                        IAppComponent.class
                )
                .body(
                        "@$AS",
                        "@Component(",
                        "   modules = {",
                        "       $AM.class,",
                        "   }",
                        ")",
                        "public interface $T extends IAppComponent {",
                        "",
                        "   void inject(TestApp testApp);",
                        "",
                        "}"
                );

        assertFiles(EMPTY_KNIGHT_APP)
                .compilesWithoutError()
                .and()
                .generatesSources(component);
    }

    @Test
    public void oneActivity() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "}"
                );

        JavaFileObject component = generatedFile(P_KNIGHT_COMPONENT, "ApplicationComponent")
                .imports(
                        EMPTY_KNIGHT_APP,
                        P_KNIGHT_MODULE + ".ApplicationModule",
                        P_KNIGHT_MODULE + ".ScreenModuleForMainActivity",
                        AppScope.class, "AS",
                        Component.class,
                        IAppComponent.class
                )
                .body(
                        "@AppScope",
                        "@Component(",
                        "   modules = {",
                        "       ApplicationModule.class,",
                        "   }",
                        ")",
                        "public interface $T extends IAppComponent {",
                        "",
                        "   void inject(TestApp testApp);",
                        "",
                        "   ScreenComponentForMainActivity plus(ScreenModuleForMainActivity module1);",
                        "",
                        "}"
                );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(component);
    }

    @Test
    public void oneActivityWithExtraModule() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "}"
                );

        JavaFileObject module = file("com.example", "ScreenModule")
                .imports(
                        Module.class,
                        ScreenProvided.class
                )
                .body(
                        "@ScreenProvided(MainActivity.class)",
                        "@Module",
                        "public class $T {",
                        "}"
                );

        JavaFileObject component = generatedFile(P_KNIGHT_COMPONENT, "ApplicationComponent")
                .imports(
                        module,
                        EMPTY_KNIGHT_APP,
                        P_KNIGHT_MODULE + ".ApplicationModule",
                        P_KNIGHT_MODULE + ".ScreenModuleForMainActivity",
                        AppScope.class,
                        Component.class,
                        IAppComponent.class
                )
                .body(
                        "@AppScope",
                        "@Component(",
                        "   modules = {",
                        "       ApplicationModule.class,",
                        "   }",
                        ")",
                        "public interface $T extends IAppComponent {",
                        "",
                        "   void inject(TestApp testApp);",
                        "",
                        "   ScreenComponentForMainActivity plus(ScreenModuleForMainActivity module1, ScreenModule module2);",
                        "",
                        "}"
                );

        assertFiles(EMPTY_KNIGHT_APP, activity, module)
                .compilesWithoutError()
                .and()
                .generatesSources(component);
    }

}
