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

    @ScreenProvided(
            in = MainActivity.class,
            scoped = true,
            named = "a"
    )
    public static int getString() {
        return new Random().nextInt();
    }

}
