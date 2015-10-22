package eu.inloop.knight.sample.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Random;

import eu.inloop.knight.IStateful;
import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.sample.MainActivity;

/**
 * Class {@link Test}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
public class Test implements Serializable {

    public static class A implements IStateful {

        @ScreenProvided(in = MainActivity.class)
        public A() {
        }

        @Override
        public void onSave(@NonNull Bundle outState) {

        }

        @Override
        public void onCreate(@Nullable Bundle savedState) {

        }
    }

    @ScreenProvided(
            in = MainActivity.class,
            scoped = true,
            named = "a"
    )
    public static int getInt(double d) {
        return new Random().nextInt();
    }

    @ScreenProvided(in = MainActivity.class)
    public static String getString() {
        return "Test";
    }

    @ScreenProvided(in = MainActivity.class)
    public static double getDouble() {
        return 3d;
    }

    @ScreenProvided(in = MainActivity.class)
    public static A getA() {
        return new A();
    }

}
