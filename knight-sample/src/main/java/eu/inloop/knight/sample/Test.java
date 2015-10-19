package eu.inloop.knight.sample;

import java.util.Random;

import eu.inloop.knight.ScreenProvided;

/**
 * Class {@link Test}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class Test {

    private static final String NAMED_1 = "123";

    @ScreenProvided(
            in = MainActivity.class,
            scoped = false
    )
    public static String getString() {
        return "Hello My World! " + new Random().nextInt();
    }

}
