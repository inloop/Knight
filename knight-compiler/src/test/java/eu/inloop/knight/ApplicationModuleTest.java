package eu.inloop.knight;

import android.app.Application;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import org.junit.Test;

import javax.inject.Named;
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

    public static final String GENERAL_LINES = join(
            "@Module",
            "public final class $T {",
            "",
            "   private final $App mApplication;",
            "",
            "   public $T($App application) {",
            "       mApplication = application;",
            "   }",
            "",
            "   @Provides",
            "   public $App provides$App() {",
            "       return mApplication;",
            "   }",
            "",
            "   @Provides",
            "   public Application providesApplication() {",
            "       return mApplication;",
            "   }",
            "",
            "   @Provides",
            "   public Resources providesApplicationResources() {",
            "       return mApplication.getResources();",
            "   }",
            ""
    );

    @Test
    public void noProvided() {
        JavaFileObject module = generatedFile(P_KNIGHT_MODULE, "ApplicationModule")
                .imports(
                        EMPTY_KNIGHT_APP, "App",
                        Application.class,
                        Resources.class,
                        Provides.class,
                        Module.class
                )
                .body(
                        GENERAL_LINES,
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
                        EMPTY_KNIGHT_APP, "App",
                        util, "U",
                        Application.class,
                        Resources.class,
                        AppScope.class, "AS",
                        Provides.class,
                        Module.class
                )
                .body(
                        GENERAL_LINES,
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

    // FIXME : fails because @Nullable is not transferred to module
    @Test
    public void named() {
        JavaFileObject util = file("com.example", "Util")
                .imports(
                        Application.class,
                        AppProvided.class, "AP",
                        Singleton.class,
                        Named.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$AP",
                        "   @Named(\"some-name\")",
                        "   @Singleton",
                        "   public $T(Application app) {}",
                        "",
                        "}"
                );
        JavaFileObject something = file("com.example", "Something")
                .imports(
                        AppProvided.class, "AP",
                        Nullable.class
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$AP",
                        "   @Nullable",
                        "   public static $T build() {",
                        "       return null;",
                        "   }",
                        "",
                        "}"
                );

        JavaFileObject module = generatedFile(P_KNIGHT_MODULE, "ApplicationModule")
                .imports(
                        EMPTY_KNIGHT_APP, "App",
                        util, "U",
                        something, "S",
                        Application.class,
                        Resources.class,
                        AppScope.class, "AS",
                        Provides.class,
                        Module.class,
                        Named.class,
                        Nullable.class
                )
                .body(
                        GENERAL_LINES,
                        "",
                        "   @Provides",
                        "   @$AS",
                        "   @Named(\"some-name\")",
                        "   public $U provides$U(Application app) {",
                        "       return new $U(app);",
                        "   }",
                        "",
                        "   @Provides",
                        "   @Nullable",
                        "   public $S providesBuild() {",
                        "       return $S.build();",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(EMPTY_KNIGHT_APP, util, something)
                .compilesWithoutError()
                .and()
                .generatesSources(module);
    }

}
