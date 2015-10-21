package eu.inloop.knight.sample.util;

import android.app.Application;

import eu.inloop.knight.AppProvided;
import eu.inloop.knight.sample.R;

/**
 * Class {@link StringUtil}
 *
 * @author FrantisekGazo
 * @version 2015-10-21
 */
public class StringUtil {

    private Application mApp;

    @AppProvided
    public StringUtil(Application app) {
        mApp = app;
    }

    public String doSomething() {
        return String.format("Hello, I have your %s App :)", mApp.getString(R.string.app_name));
    }

}
