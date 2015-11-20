package eu.inloop.knight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.annotation.Generated;
import javax.tools.JavaFileObject;

/**
 * Class {@link NavigatorTest}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-20
 */
public class NavigatorTest extends BaseTest {

    @Test
    public void oneActivityNoExtra() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString(KNIGHT + "I",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT,
                        "",
                        importClass(
                                "com.example.MainActivity",
                                Intent.class,
                                Context.class,
                                Generated.class
                        ),
                        "",
                        GENERATED,
                        "public final class I {",
                        "",
                        "  public static Intent forMainActivity(Context context) {",
                        "    Intent intent = new Intent(context, MainActivity.class);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startMainActivity(Context context) {",
                        "    context.startActivity(forMainActivity(context));",
                        "  }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivityOneExtraPrimitive() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class,
                                Extra.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "",
                        "   @Extra",
                        "   int number;",
                        "",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString(KNIGHT + "I",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT,
                        "",
                        importClass(
                                "com.example.MainActivity",
                                Intent.class,
                                Context.class,
                                Generated.class
                        ),
                        "",
                        GENERATED,
                        "public final class I {",
                        "",
                        "  public static Intent forMainActivity(Context context, int number) {",
                        "    Intent intent = new Intent(context, MainActivity.class);",
                        "    intent.putExtra(\"MainActivity.EXTRA_number\", number);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startMainActivity(Context context, int number) {",
                        "    context.startActivity(forMainActivity(context, number));",
                        "  }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivityOneExtraObject() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class,
                                Extra.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "",
                        "   @Extra",
                        "   String mText;",
                        "",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString(KNIGHT + "I",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT,
                        "",
                        importClass(
                                "com.example.MainActivity",
                                Intent.class,
                                Context.class,
                                Generated.class,
                                String.class
                        ),
                        "",
                        GENERATED,
                        "public final class I {",
                        "",
                        "  public static Intent forMainActivity(Context context, String mText) {",
                        "    Intent intent = new Intent(context, MainActivity.class);",
                        "    intent.putExtra(\"MainActivity.EXTRA_mText\", mText);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startMainActivity(Context context, String mText) {",
                        "    context.startActivity(forMainActivity(context, mText));",
                        "  }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivityOneExtraObjectNamed() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class,
                                Extra.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "",
                        "   @Extra(\"somethingElse\")",
                        "   Double mSomething;",
                        "",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString(KNIGHT + "I",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT,
                        "",
                        importClass(
                                "com.example.MainActivity",
                                Intent.class,
                                Context.class,
                                Generated.class,
                                Double.class
                        ),
                        "",
                        GENERATED,
                        "public final class I {",
                        "",
                        "  public static Intent forMainActivity(Context context, Double somethingElse) {",
                        "    Intent intent = new Intent(context, MainActivity.class);",
                        "    intent.putExtra(\"MainActivity.EXTRA_somethingElse\", somethingElse);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startMainActivity(Context context, Double somethingElse) {",
                        "    context.startActivity(forMainActivity(context, somethingElse));",
                        "  }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void twoActivitiesWithExtras() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class,
                                Extra.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "",
                        "   @Extra(\"number\")",
                        "   Double mNumber;",
                        "",
                        "   @Extra",
                        "   Float mSomething;",
                        "",
                        "}"
                )
        );
        JavaFileObject secondActivity = JavaFileObjects.forSourceString("com.example.SecondActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class,
                                Extra.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class SecondActivity extends Activity {",
                        "",
                        "   @Extra(\"hasFlag\")",
                        "   boolean mFlag;",
                        "",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString(KNIGHT + "I",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT,
                        "",
                        importClass(
                                "com.example.MainActivity",
                                "com.example.SecondActivity",
                                Intent.class,
                                Context.class,
                                Generated.class,
                                Double.class,
                                Float.class
                        ),
                        "",
                        GENERATED,
                        "public final class I {",
                        "",
                        "  public static Intent forMainActivity(Context context, Double number, Float mSomething) {",
                        "    Intent intent = new Intent(context, MainActivity.class);",
                        "    intent.putExtra(\"MainActivity.EXTRA_number\", number);",
                        "    intent.putExtra(\"MainActivity.EXTRA_mSomething\", mSomething);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startMainActivity(Context context, Double number, Float mSomething) {",
                        "    context.startActivity(forMainActivity(context, number, mSomething));",
                        "  }",
                        "",
                        "  public static Intent forSecondActivity(Context context, boolean hasFlag) {",
                        "    Intent intent = new Intent(context, SecondActivity.class);",
                        "    intent.putExtra(\"SecondActivity.EXTRA_hasFlag\", hasFlag);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startSecondActivity(Context context, boolean hasFlag) {",
                        "    context.startActivity(forSecondActivity(context, hasFlag));",
                        "  }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP, activity, secondActivity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void threeActivitiesWithExtras() {
        JavaFileObject activity = JavaFileObjects.forSourceString("com.example.MainActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class,
                                Extra.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class MainActivity extends Activity {",
                        "",
                        "   @Extra(\"number\")",
                        "   Double mNumber;",
                        "",
                        "   @Extra",
                        "   Float mSomething;",
                        "",
                        "}"
                )
        );
        JavaFileObject secondActivity = JavaFileObjects.forSourceString("com.example.SecondActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class,
                                Extra.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class SecondActivity extends Activity {",
                        "",
                        "   @Extra(\"hasFlag\")",
                        "   boolean mFlag;",
                        "",
                        "}"
                )
        );
        JavaFileObject thirdActivity = JavaFileObjects.forSourceString("com.example.ThirdActivity",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Activity.class,
                                KnightActivity.class
                        ),
                        "",
                        "@KnightActivity",
                        "public class ThirdActivity extends Activity {",
                        "}"
                )
        );

        JavaFileObject navigator = JavaFileObjects.forSourceString(KNIGHT + "I",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT,
                        "",
                        importClass(
                                "com.example.MainActivity",
                                "com.example.SecondActivity",
                                "com.example.ThirdActivity",
                                Intent.class,
                                Context.class,
                                Generated.class,
                                Double.class,
                                Float.class
                        ),
                        "",
                        GENERATED,
                        "public final class I {",
                        "",
                        "  public static Intent forMainActivity(Context context, Double number, Float mSomething) {",
                        "    Intent intent = new Intent(context, MainActivity.class);",
                        "    intent.putExtra(\"MainActivity.EXTRA_number\", number);",
                        "    intent.putExtra(\"MainActivity.EXTRA_mSomething\", mSomething);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startMainActivity(Context context, Double number, Float mSomething) {",
                        "    context.startActivity(forMainActivity(context, number, mSomething));",
                        "  }",
                        "",
                        "  public static Intent forSecondActivity(Context context, boolean hasFlag) {",
                        "    Intent intent = new Intent(context, SecondActivity.class);",
                        "    intent.putExtra(\"SecondActivity.EXTRA_hasFlag\", hasFlag);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startSecondActivity(Context context, boolean hasFlag) {",
                        "    context.startActivity(forSecondActivity(context, hasFlag));",
                        "  }",
                        "",
                        "  public static Intent forThirdActivity(Context context) {",
                        "    Intent intent = new Intent(context, ThirdActivity.class);",
                        "    return intent;",
                        "  }",
                        "",
                        "  public static void startThirdActivity(Context context) {",
                        "    context.startActivity(forThirdActivity(context));",
                        "  }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP, activity, secondActivity, thirdActivity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

}
