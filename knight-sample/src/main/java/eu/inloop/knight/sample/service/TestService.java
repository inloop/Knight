package eu.inloop.knight.sample.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import eu.inloop.knight.sample.util.NetUtils;

/**
 * Class {@link TestService}
 *
 * @author FrantisekGazo
 * @version 2015-11-18
 */
public class TestService extends IntentService {

    public static final String L = "TestServiceLog";

    @Inject
    NetUtils mNetUtils;

    public static Intent newIntent(Context context) {
        return new Intent(context, TestService.class);
    }

    public TestService() {
        super("Test");
        Log.d(L, "constructor with " + mNetUtils);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(L, "onHandleIntent with " + mNetUtils);
    }

}