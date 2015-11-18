package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;

import dagger.internal.codegen.ComponentProcessor;

/**
 * Class {@link KnightTest}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-20
 */
public class KnightTest {

    private Iterable<JavaFileObject> files(JavaFileObject... f) {
        List<JavaFileObject> files = new ArrayList<>();
        files.addAll(Arrays.asList(f));
        return files;
    }

    private CompileTester assertFiles(JavaFileObject... f) {
        return Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files(f))
                .processedWith(new ComponentProcessor(), new KnightProcessor());
    }

    private JavaFileObject exampleApp() {
        return JavaFileObjects.forSourceString("com.example.ExampleApp",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Application;",
                        "import eu.inloop.knight.KnightApp;",
                        "",
                        "@KnightApp",
                        "public class ExampleApp extends Application {",
                        "}"
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
                        "",
                        "@KnightActivity",
                        "public class ExampleActivity extends Activity {",
                        "}"
                )
        );
    }

    @Test
    public void knightClass() {
        JavaFileObject navigator = JavaFileObjects.forSourceString("the.knight.I",
                Joiner.on('\n').join(
                        "package the.knight;",
                        "",
                        "import android.content.Context;",
                        "import android.content.Intent;",
                        "import com.example.ExampleActivity;",
                        "import javax.annotation.Generated;",
                        "",
                        "@Generated(\"eu.inloop.knight.KnightProcessor\")",
                        "public final class I {",
                        "",
                        "  public static Intent forExampleActivity(Context context) {",
                        "    Intent intent = new Intent(context, ExampleActivity.class);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startExampleActivity(Context context) {",
                        "    context.startActivity(forExampleActivity(context));",
                        "  }",
                        "",
                        "}"
                )
        );

        assertFiles(exampleApp(), exampleActivity())
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

}
