package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;

/**
 * Class {@link KnightTest}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-20
 */
public class KnightTest {

    private JavaFileObject exampleActivity() {
        return JavaFileObjects.forSourceString("com.example.ExampleActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import eu.inloop.knight.Scoped;",
                        "",
                        "@Scoped",
                        "public class ExampleActivity {",
                        "}"
                )
        );
    }

    private Iterable<JavaFileObject> files(JavaFileObject... f) {
        List<JavaFileObject> files = new ArrayList<>();
            files.add(exampleActivity());
        files.addAll(Arrays.asList(f));
        return files;
    }

    @Test
    public void knightClass() {
        JavaFileObject knightFile = JavaFileObjects.forSourceString("the.knight.Knight",
                Joiner.on('\n').join(
                        "package the.knight;",
                        "",
                        "public final class Knight {",
                        "",
                        "}"
                )
        );
        JavaFileObject appComponentFile = JavaFileObjects.forSourceString("the.knight.di.module",
                Joiner.on('\n').join(
                        "package the.knight.di.component;",
                        "",
                        "public final class ApplicationComponent {",
                        "",
                        "}"
                )
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files())
                .processedWith(new KnightProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(knightFile)
                .and()
                .generatesSources(appComponentFile);
    }

}
