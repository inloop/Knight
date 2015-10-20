package eu.inloop.knight;

import android.util.Log;

/**
 * Class {@link LogHelper}
 *
 * @author FrantisekGazo
 * @version 2015-10-20
 */
class LogHelper {

    private static final boolean LOG = true;
    private static final String TAG = "Knight Log";

    public static void d(String msg, Object... args) {
        if (LOG) {
            Log.d(TAG, String.format(msg, args));
        }
    }

}
