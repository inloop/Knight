package eu.inloop.knight.sample;

import eu.inloop.knight.ActivityProvided;

/**
 * Class {@link ActTest}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class ActTest {

    private static final String NAMED_1 = "123";

    @ActivityProvided(in = MainActivity.class)
    public ActTest() {

    }

    @ActivityProvided(named = NAMED_1, in = MainActivity.class)
    public ActTest(int i) {

    }

    @ActivityProvided(scoped = true, in = MainActivity.class)
    public static int test(int a, String b) {
        return 0;
    }

    @ActivityProvided(
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

}
