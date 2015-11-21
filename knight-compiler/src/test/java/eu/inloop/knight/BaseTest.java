package eu.inloop.knight;

import android.app.Application;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;

import dagger.internal.codegen.ComponentProcessor;
import eu.inloop.knight.builder.GPN;

import static eu.inloop.knight.builder.GPN.COMPONENTS;
import static eu.inloop.knight.builder.GPN.DI;
import static eu.inloop.knight.builder.GPN.FACTORIES;
import static eu.inloop.knight.builder.GPN.KNIGHT;
import static eu.inloop.knight.builder.GPN.MODULES;
import static eu.inloop.knight.util.File.file;

/**
 * Class {@link BaseTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-19
 */
public class BaseTest {

    protected static final String C_NAVIGATOR = "I";

    protected static final String P_KNIGHT = GPN.toString(KNIGHT);
    protected static final String P_KNIGHT_FACTORY = GPN.toString(KNIGHT, DI, FACTORIES);
    protected static final String P_KNIGHT_MODULE = GPN.toString(KNIGHT, DI, MODULES);
    protected static final String P_KNIGHT_COMPONENT = GPN.toString(KNIGHT, DI, COMPONENTS);

    protected static final JavaFileObject EMPTY_KNIGHT_APP = file("com.example", "TestApp")
            .imports(
                    Application.class,
                    KnightApp.class
            )
            .body(
                    "@KnightApp",
                    "public class $T extends Application {}"
            );

    protected CompileTester assertFiles(JavaFileObject... f) {
        List<JavaFileObject> files = new ArrayList<>();
        files.addAll(Arrays.asList(f));

        return Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files)
                .processedWith(new ComponentProcessor(), new KnightProcessor());
    }

}
