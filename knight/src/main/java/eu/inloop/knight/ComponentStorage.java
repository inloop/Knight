package eu.inloop.knight;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eu.inloop.knight.core.IActivityComponent;
import eu.inloop.knight.core.IAppComponent;
import eu.inloop.knight.core.IScreenComponent;

/**
 * Class {@link ComponentStorage}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public abstract class ComponentStorage<C extends IAppComponent> implements Application.ActivityLifecycleCallbacks {

    private static final String EXTRA_ACTIVITY_ID = "eu.inloop.knight.ACTIVITY_ID";
    private final Map<String, String> mHashToUUID = new HashMap<>();

    private C mApplicationComponent;
    private final Map<String, IScreenComponent> mScreenComponents = new HashMap<>();
    private final Map<String, IActivityComponent> mActivityComponents = new HashMap<>();

    public ComponentStorage(C mApplicationComponent) {
        this.mApplicationComponent = mApplicationComponent;
    }

    public C getApplicationComponent() {
        return mApplicationComponent;
    }

    public IScreenComponent getScreenComponent(String uuid) {
        return mScreenComponents.get(uuid);
    }

    public IActivityComponent getActivityComponent(String uuid) {
        return mActivityComponents.get(uuid);
    }

    public void putScreenComponent(String uuid, IScreenComponent component) {
        mScreenComponents.put(uuid, component);
    }

    public void putActivityComponent(String uuid, IActivityComponent component) {
        mActivityComponents.put(uuid, component);
    }

    public void removeScreenComponent(String uuid) {
        mScreenComponents.remove(uuid);
    }

    public void removeActivityComponent(String uuid) {
        mActivityComponents.remove(uuid);
    }

    private void removeUuidMappingFor(Activity activity) {
        mHashToUUID.remove(getActivityHash(activity));
    }

    protected abstract boolean isScoped(Class activityClass);

    protected abstract void callbackOnActivityCreated(Activity activity, Bundle savedInstanceState, String uuid);

    protected abstract void callbackOnActivityDestroyed(Activity activity, String uuid);

    protected abstract void callbackOnActivitySaved(Activity activity, Bundle outState, String uuid);

    private String getActivityHash(Activity activity) {
        return activity.toString(); // TODO : find better way to get unique id from activity -> .hashCode() is 'int' ?
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (isScoped(activity.getClass())) {
            String uuid = null;
            if (savedInstanceState != null) {
                uuid = savedInstanceState.getString(EXTRA_ACTIVITY_ID);
            }
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }
            mHashToUUID.put(getActivityHash(activity), uuid);
            callbackOnActivityCreated(activity, savedInstanceState, uuid);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (isScoped(activity.getClass())) {
            String uuid = mHashToUUID.get(getActivityHash(activity));
            if (uuid != null) {
                outState.putString(EXTRA_ACTIVITY_ID, uuid);
                callbackOnActivitySaved(activity, outState, uuid);
            }
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (isScoped(activity.getClass())) {
            String uuid = mHashToUUID.get(getActivityHash(activity));
            if (uuid != null) {
                callbackOnActivityDestroyed(activity, uuid);
                removeUuidMappingFor(activity); // TODO : check if onDestroy() is called after onSaveInstanceState()
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }
}
