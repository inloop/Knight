package eu.inloop.knight.sample;

import android.app.Application;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import eu.inloop.knight.KnightApp;

/**
 * Class {@link App}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
@KnightApp
public class App extends Application {

    @Inject
    EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus.post("Hello World!");
    }

}
