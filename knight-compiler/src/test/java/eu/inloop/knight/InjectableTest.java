package eu.inloop.knight;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;

/**
 * Class {@link InjectableTest}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-20
 */
public class InjectableTest {

    private Iterable<JavaFileObject> files(JavaFileObject... f) {
        List<JavaFileObject> files = new ArrayList<>();
        files.addAll(Arrays.asList(f));
        return files;
    }

    //@Test
    public void invalidFrom() {
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
        JavaFileObject injectable = JavaFileObjects.forSourceString("com.example.ExampleInjectable",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import eu.inloop.knight.Injectable;",
                        "",
                        "@Injectable(from = String.class)",
                        "public class ExampleInjectable {",
                        "}"
                )
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files(activity, injectable))
                .processedWith(new KnightProcessor())
                .failsToCompile()
                .withErrorContaining(ErrorMsg.Injectable_outside_Scoped_Activity.toString());
    }

    //@Test
    public void invalidActivity() {
        JavaFileObject activity1 = JavaFileObjects.forSourceString("com.example.ExampleActivity1",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Activity;",
                        "import eu.inloop.knight.Scoped;",
                        "",
                        "@Scoped",
                        "public class ExampleActivity1 extends Activity {",
                        "}"
                )
        );
        JavaFileObject activity2 = JavaFileObjects.forSourceString("com.example.ExampleActivity2",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import android.app.Activity;",
                        "",
                        "public class ExampleActivity2 extends Activity {",
                        "}"
                )
        );
        JavaFileObject injectable = JavaFileObjects.forSourceString("com.example.ExampleInjectable",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import eu.inloop.knight.Injectable;",
                        "",
                        "@Injectable(from = ExampleActivity2.class)",
                        "public class ExampleInjectable {",
                        "}"
                )
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files(activity1, activity2, injectable))
                .processedWith(new KnightProcessor())
                .failsToCompile()
                .withErrorContaining(ErrorMsg.Injectable_outside_Scoped_Activity.toString());
    }

    //@Test
    public void valid() {
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
        JavaFileObject injectable = JavaFileObjects.forSourceString("com.example.ExampleInjectable",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        "import eu.inloop.knight.Injectable;",
                        "",
                        "@Injectable(from = ExampleActivity.class)",
                        "public class ExampleInjectable {",
                        "}"
                )
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(files(activity, injectable))
                .processedWith(new KnightProcessor())
                .compilesWithoutError();
    }

}
