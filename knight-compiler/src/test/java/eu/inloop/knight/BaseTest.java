package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    protected static final String GENERATED = "@Generated(\"eu.inloop.knight.KnightProcessor\")";

    private static final String PACKAGE = "package %s;";

    protected static final String KNIGHT = "the.knight.";
    protected static final String KNIGHT_MODULE = KNIGHT + "di.module.";
    protected static final String KNIGHT_COMPONENT = KNIGHT + "di.component.";

    protected static final String PACKAGE_KNIGHT = pack(KNIGHT);
    protected static final String PACKAGE_KNIGHT_COMPONENT = pack(KNIGHT_COMPONENT);
    protected static final String PACKAGE_KNIGHT_MODULE = pack(KNIGHT_MODULE);

    protected static String pack(String pack) {
        return String.format(PACKAGE, pack.substring(0, pack.length() - 1)); // ignore last "."
    }

    protected static final JavaFileObject EMPTY_KNIGHT_APP = JavaFileObjects.forSourceString("com.example.TestApp",
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

    protected String importClass(String c) {
        return String.format("import %s;", c);
    }

    protected String importClass(Object... objects) {
        List<String> classes = new ArrayList<>(objects.length);
        for (Object o : objects) {
            if (o instanceof Class) {
                classes.add(((Class) o).getName());
            } else if (o instanceof String) {
                classes.add((String) o);
            }
        }
        Collections.sort(classes);
        StringBuilder sb = new StringBuilder();
        for (String i : classes) {
            sb.append(importClass(i)).append("\n");
        }
        return sb.toString();
    }

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

}
