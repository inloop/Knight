package eu.inloop.knight.sample;

import eu.inloop.knight.AppProvided;

/**
 * Class {@link Test}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class Test {

    private static final String NAMED_1 = "123";

    @AppProvided
    public Test() {

    }

    @AppProvided(scoped = true, named = NAMED_1)
    public Test(int i) {

    }

    @AppProvided
    public static int test(int a, String b) {
        return 0;
    }

    @AppProvided(named = "else")
    public static int something(int a, String b) {
        return 0;
    }

    @AppProvided
    public static int something2(int a, String b) {
        return 0;
    }

}
