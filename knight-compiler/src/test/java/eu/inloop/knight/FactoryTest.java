package eu.inloop.knight;

import android.app.Application;

import org.junit.Test;

import javax.inject.Provider;
import javax.tools.JavaFileObject;

import eu.inloop.knight.assisted.Assisted;
import eu.inloop.knight.assisted.Factory;

import static eu.inloop.knight.util.File.file;
import static eu.inloop.knight.util.File.generatedFile;

/**
 * Class {@link FactoryTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-22
 */
public class FactoryTest extends BaseTest {

    @Test
    public void s() {
        JavaFileObject something = file("com.example", "Something")
                .imports(
                        Application.class,
                        AppProvided.class, "AP",
                        Assisted.class, "A",
                        Factory.class, "F"
                )
                .body(
                        "public class $T {",
                        "",
                        "   @$F",
                        "   @$AP",
                        "   public $T(Application app, @$A String s) {}",
                        "",
                        "}"
                );

        JavaFileObject factory = generatedFile("com.example", "SomethingFactory")
                .imports(
                        something, "S",
                        Application.class,
                        String.class,
                        Provider.class
                )
                .body(
                        "public final class $T {",
                        "",
                        "   private final Provider<Application> appProvider;",
                        "",
                        "   public $T(Provider<Application> appProvider) {",
                        "       this.appProvider = appProvider;",
                        "   }",
                        "",
                        "   public $S get(String s) {",
                        "       return new $S(appProvider.get(), s);",
                        "   }",
                        "",
                        "}"
                );

        assertFiles(EMPTY_KNIGHT_APP, something)
                .compilesWithoutError()
                .and()
                .generatesSources(factory);
    }
}
