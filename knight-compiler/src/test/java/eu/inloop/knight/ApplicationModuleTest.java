package eu.inloop.knight;

import android.app.Application;

import org.junit.Test;

import javax.inject.Singleton;
import javax.tools.JavaFileObject;

import dagger.Module;
import dagger.Provides;
import eu.inloop.knight.scope.AppScope;

import static eu.inloop.knight.util.File.file;
import static eu.inloop.knight.util.File.generatedFile;

/**
 * Class {@link ApplicationModuleTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-19
 */
public class ApplicationModuleTest extends BaseTest {

    @Test
    public void noProvided() {
        JavaFileObject module = generatedFile(P_KNIGHT_MODULE, "ApplicationModule")
                .imports(
                        Application.class,
                        Provides.class,
                        Module.class
                )
                .body(
                        "@Module",
                        "public final class $T {",
                        "",
                        "   private final Application mApplication;",
                        "",
                        "   public $T(Application application) {",
                        "       mApplication = application;",
                        "   }",
                        "",
                        "   @Provides",
                        "   public Application providesApplication() {",
                        "       return mApplication;",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(EMPTY_KNIGHT_APP)
                .compilesWithoutError()
                .and()
                .generatesSources(module);
    }

    @Test
    public void util() {
        JavaFileObject util = file("com.example", "Util")
                .imports(
                        Application.class,
                        AppProvided.class, "AP",
                        Singleton.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$AP",
                        "   @Singleton",
                        "   public $T(Application app) {}",
                        "",
                        "}"
                );

        JavaFileObject module = generatedFile(P_KNIGHT_MODULE, "ApplicationModule")
                .imports(
                        util, "U",
                        Application.class,
                        AppScope.class, "AS",
                        Provides.class,
                        Module.class
                )
                .body(
                        "@Module",
                        "public final class $T {",
                        "",
                        "   private final Application mApplication;",
                        "",
                        "   public $T(Application application) {",
                        "       mApplication = application;",
                        "   }",
                        "",
                        "   @Provides",
                        "   public Application providesApplication() {",
                        "       return mApplication;",
                        "   }",
                        "",
                        "   @Provides",
                        "   @$AS",
                        "   public $U provides$U(Application app) {",
                        "       return new $U(app);",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(EMPTY_KNIGHT_APP, util)
                .compilesWithoutError()
                .and()
                .generatesSources(module);
    }

}
