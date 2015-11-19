package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;

import dagger.internal.codegen.ComponentProcessor;

/**
 * Class {@link BaseTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-19
 */
public class BaseTest {

    protected Iterable<JavaFileObject> files(JavaFileObject... f) {
        List<JavaFileObject> files = new ArrayList<>();
        files.addAll(Arrays.asList(f));
        return files;
    }

    protected CompileTester assertFiles(JavaFileObject... f) {
        return Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files(f))
                .processedWith(new ComponentProcessor(), new KnightProcessor());
    }

    protected JavaFileObject emptyKnightApp() {
        return JavaFileObjects.forSourceString("com.example.TestApp",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Application;",
                        "import eu.inloop.knight.KnightApp;",
                        "",
                        "@KnightApp",
                        "public class TestApp extends Application {",
                        "}"
                )
        );
    }

}
