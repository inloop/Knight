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

    /**
     * Does NOT manage given object.
     */
    public <T> T manage(String id, T obj) {
        // ignore
        return obj;
    }

    /**
     * Manages given object.
     *
     * @param id  ID of managed object.
     * @param obj Managed object.
     * @return Given object.
     */
    public <T extends IStateful> T manage(String id, T obj) {
        mStatefulMap.put(id, obj);
        restoreState(id, obj);
        return obj;
    }

    /**
     * Saves all managed objects into given {@link Bundle}.
     */
    public void saveInto(Bundle outState) {
        for (Map.Entry<String, IStateful> entry : mStatefulMap.entrySet()) {
            outState.putBundle(entry.getKey(), saveState(entry.getValue()));
        }
    }

    /**
     * Clears managed objects.
     */
    public void clear() {
        mBundle = null;
        notifyOnRemove();
        mStatefulMap.clear();
    }

    /**
     * Restores state of one managed object.
     *
     * @param id  ID of managed object.
     * @param obj Managed object.
     */
    private void restoreState(String id, IStateful obj) {
        Bundle objBundle = null;
        if (mBundle != null) {
            objBundle = mBundle.getBundle(id);
        }
        obj.onCreate(objBundle);
    }

    /**
     * Saves state of one managed object.
     *
     * @param obj Managed object.
     * @return {@link Bundle} to which the
     */
    private Bundle saveState(IStateful obj) {
        Bundle objBundle = new Bundle();
        obj.onSave(objBundle);
        return objBundle;
    }

    /**
     * Notifies all managed objects that they will be removed.
     */
    private void notifyOnRemove() {
        for (Map.Entry<String, IStateful> entry : mStatefulMap.entrySet()) {
            entry.getValue().onRemove();
        }
    }

}
