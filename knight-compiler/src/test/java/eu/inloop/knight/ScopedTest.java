package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;

import org.junit.Test;

import javax.tools.JavaFileObject;

/**
 * Class {@link ScopedTest}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-20
 */
public class ScopedTest {

    @Test
    public void invalidScopedClass() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.ExampleActivity",
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

        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(activity)
                .processedWith(new KnightProcessor())
                .failsToCompile()
                .withErrorContaining(ErrorMsg.Scoped_can_be_only_Activity.toString());
    }

    @Test
    public void scopedActivity() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.ExampleActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Activity;",
                        "import eu.inloop.knight.Scoped;",
                        "",
                        "@Scoped",
                        "public class ExampleActivity extends Activity {",
                        "}"
                )
        );

        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(activity)
                .processedWith(new KnightProcessor())
                .compilesWithoutError();
    }

}