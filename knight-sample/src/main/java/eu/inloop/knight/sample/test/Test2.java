package eu.inloop.knight.sample.test;

import java.util.Random;

import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.sample.MainActivity;

/**
 * Class {@link Test2}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class Test2 {

    @ScreenProvided(in = MainActivity.class, scoped = true)
    public static int getnumber() {
        return new Random().nextInt();
    }

}
