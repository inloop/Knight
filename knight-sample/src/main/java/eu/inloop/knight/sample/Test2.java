package eu.inloop.knight.sample;

import java.util.Random;

import eu.inloop.knight.ScreenProvided;

/**
 * Class {@link Test2}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class Test2 {

    @ScreenProvided(
            in = MainActivity.class,
            scoped = true
    )
    public static int getString() {
        return new Random().nextInt();
    }

}
