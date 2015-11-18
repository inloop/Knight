package eu.inloop.knight.sample.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Singleton;

import eu.inloop.knight.AppProvided;

/**
 * Class {@link NetUtils}.
 *
 * @author f3rog
 * @version 2015-07-11
 */
public class NetUtils {

    private Context mAppContext;

    @AppProvided
    @Singleton
    public NetUtils(Application app) {
        this.mAppContext = app;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
