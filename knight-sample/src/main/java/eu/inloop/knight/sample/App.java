package eu.inloop.knight.sample;

import android.app.Application;

import the.knight.Knight;

/**
 * Class {@link App}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Knight.braceYourselfFor(this);
    }

}
