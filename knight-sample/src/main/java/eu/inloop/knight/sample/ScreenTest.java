package eu.inloop.knight.sample;

import eu.inloop.knight.ScreenProvided;

/**
 * Class {@link ScreenTest}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class ScreenTest {

    private static final String NAMED_1 = "123";

    @ScreenProvided(in = MainActivity.class)
    public ScreenTest() {

    }

    @ScreenProvided(named = NAMED_1, in = MainActivity.class)
    public ScreenTest(int i) {

    }

    @ScreenProvided(scoped = true, in = MainActivity.class)
    public static int test(int a, String b) {
        return 0;
    }

    @ScreenProvided(
            named = "test",
            scoped = true,
            in = {
                    MainActivity.class,
                    SecondActivity.class
            }
    )
    public static int something(int a, String b) {
        return 0;
    }

    @ScreenProvided(in = MainActivity.class)
    public static int something2(int a, String b) {
        return 0;
    }

}
