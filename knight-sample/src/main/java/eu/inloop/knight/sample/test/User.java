package eu.inloop.knight.sample.test;

import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.sample.SecondActivity;

/**
 * Class {@link User}
 *
 * @author FrantisekGazo
 * @version 2015-10-22
 */
public class User {

    private String mName;

    @ScreenProvided(in = SecondActivity.class)
    public User(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return String.format("My name is %s", mName);
    }

}
