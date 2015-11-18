package eu.inloop.knight.sample;

import android.app.Application;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import eu.inloop.knight.KnightApp;
import eu.inloop.knight.sample.service.TestService;

/**
 * Class {@link App}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
@KnightApp({
        TestService.class
})
public class App extends Application {

    @Inject
    EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus.post("Hello World!");

        startService(TestService.newIntent(this));
    }

}
