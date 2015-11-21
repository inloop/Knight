package eu.inloop.knight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static eu.inloop.knight.util.File.file;
import static eu.inloop.knight.util.File.generatedFile;

/**
 * Class {@link NavigatorTest}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-20
 */
public class NavigatorTest extends BaseTest {

    @Test
    public void oneActivityNoExtra() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "}"
                );

        JavaFileObject navigator = generatedFile(P_KNIGHT, C_NAVIGATOR)
                .imports(
                        activity,
                        Intent.class,
                        Context.class
                )
                .body(
                        "public final class $T {",
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
                );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivityOneExtraPrimitive() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class,
                        Extra.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "",
                        "   @Extra",
                        "   int number;",
                        "",
                        "}"
                );

        JavaFileObject navigator = generatedFile(P_KNIGHT, C_NAVIGATOR)
                .imports(
                        activity,
                        Intent.class,
                        Context.class
                )
                .body(
                        "public final class $T {",
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
                );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivityOneExtraObject() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class,
                        Extra.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "",
                        "   @Extra",
                        "   String mText;",
                        "",
                        "}"
                );

        JavaFileObject navigator = generatedFile(P_KNIGHT, C_NAVIGATOR)
                .imports(
                        activity,
                        Intent.class,
                        Context.class,
                        String.class
                )
                .body(
                        "public final class $T {",
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
                );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void oneActivityOneExtraObjectNamed() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class,
                        Extra.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "",
                        "   @Extra(\"somethingElse\")",
                        "   Double mSomething;",
                        "",
                        "}"
                );

        JavaFileObject navigator = generatedFile(P_KNIGHT, C_NAVIGATOR)
                .imports(
                        activity,
                        Intent.class,
                        Context.class,
                        Double.class
                )
                .body(
                        "public final class $T {",
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
                );

        assertFiles(EMPTY_KNIGHT_APP, activity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void twoActivitiesWithExtras() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class,
                        Extra.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "",
                        "   @Extra(\"number\")",
                        "   Double mNumber;",
                        "",
                        "   @Extra",
                        "   Float mSomething;",
                        "",
                        "}"
                );
        JavaFileObject secondActivity = file("com.example", "SecondActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class,
                        Extra.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "",
                        "   @Extra(\"hasFlag\")",
                        "   boolean mFlag;",
                        "",
                        "}"
                );

        JavaFileObject navigator = generatedFile(P_KNIGHT, C_NAVIGATOR)
                .imports(
                        activity,
                        secondActivity,
                        Intent.class,
                        Context.class,
                        Double.class,
                        Float.class
                )
                .body(
                        "public final class $T {",
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
                );

        assertFiles(EMPTY_KNIGHT_APP, activity, secondActivity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

    @Test
    public void threeActivitiesWithExtras() {
        JavaFileObject activity = file("com.example", "MainActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class,
                        Extra.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "",
                        "   @Extra(\"number\")",
                        "   Double mNumber;",
                        "",
                        "   @Extra",
                        "   Float mSomething;",
                        "",
                        "}"
                );
        JavaFileObject secondActivity = file("com.example", "SecondActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class,
                        Extra.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "",
                        "   @Extra(\"hasFlag\")",
                        "   boolean mFlag;",
                        "",
                        "}"
                );
        JavaFileObject thirdActivity = file("com.example", "ThirdActivity")
                .imports(
                        Activity.class,
                        KnightActivity.class
                )
                .body(
                        "@KnightActivity",
                        "public class $T extends Activity {",
                        "}"
                );

        JavaFileObject navigator = generatedFile(P_KNIGHT, C_NAVIGATOR)
                .imports(
                        activity,
                        secondActivity,
                        thirdActivity,
                        Intent.class,
                        Context.class,
                        Double.class,
                        Float.class
                )
                .body(
                        "public final class $T {",
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
                );

        assertFiles(EMPTY_KNIGHT_APP, activity, secondActivity, thirdActivity)
                .compilesWithoutError()
                .and()
                .generatesSources(navigator);
    }

}
