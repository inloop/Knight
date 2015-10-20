package eu.inloop.knight;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Class {@link StateManager} is used for managing states of screen scoped objects implementing {@link IStateful}.
 *
 * @author FrantisekGazo
 * @version 2015-10-20
 */
public class StateManager {

    private Bundle mBundle;
    private final Map<String, IStateful> mStatefulMap = new HashMap<>();

    public void setBundle(Bundle bundle) {
        this.mBundle = bundle;
    }

    private void restoreState(String id, IStateful obj) {
        Bundle objBundle = null;
        if (mBundle != null) {
            objBundle = mBundle.getBundle(id);
        }
        obj.onCreate(objBundle);
    }

    private Bundle saveState(IStateful obj) {
        Bundle objBundle = new Bundle();
        obj.onSave(objBundle);
        return objBundle;
    }

    public <T> T manage(String id, T obj) {
        // ignore
        return obj;
    }

    public <T extends IStateful> T manage(String id, T obj) {
        mStatefulMap.put(id, obj);
        restoreState(id, obj);
        return obj;
    }

    public void saveInto(Bundle outState) {
        for (Map.Entry<String, IStateful> entry : mStatefulMap.entrySet()) {
            outState.putBundle(entry.getKey(), saveState(entry.getValue()));
        }
    }

    public void clear() {
        mBundle = null;
        mStatefulMap.clear();
    }

}
