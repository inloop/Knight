package eu.inloop.knight;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Pair;

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

    protected final C getApplicationComponent() {
        return mApplicationComponent;
    }

    protected final IActivityComponent getActivityComponent(Activity activity) {
        return mActivityComponents.get(getActivityUUID(activity));
    }

    protected abstract boolean isScoped(Class activityClass);

    protected abstract Pair<IScreenComponent, IActivityComponent> onActivityCreated(Activity activity, Bundle savedInstanceState, IScreenComponent sc);

    protected abstract void onActivitySaved(Activity activity, Bundle outState, IScreenComponent sc);

    private void removeUuidMappingFor(Activity activity) {
        mHashToUUID.remove(getActivityHash(activity));
    }

    private String getActivityHash(Activity activity) {
        return String.valueOf(activity.hashCode()); // TODO : find better way to get unique id from activity -> .hashCode() is 'int' ?
    }

    private String getActivityUUID(Activity activity) {
        return mHashToUUID.get(getActivityHash(activity));
    }

    @Override
    public final void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (isScoped(activity.getClass())) {
            //LogHelper.d("activity - %s with hash %s", activity, getActivityHash(activity));
            String uuid = null;
            LogHelper.d("onActivityCreated - hasState : %b", savedInstanceState != null);
            if (savedInstanceState != null) {
                uuid = savedInstanceState.getString(EXTRA_ACTIVITY_ID);
            }
            LogHelper.d("onActivityCreated - found UUID : %s", uuid);
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                LogHelper.d("onActivityCreated - created UUID : %s", uuid);
            }
            mHashToUUID.put(getActivityHash(activity), uuid);
            IScreenComponent sc = mScreenComponents.get(uuid);
            Pair<IScreenComponent, IActivityComponent> pair = onActivityCreated(activity, savedInstanceState, sc);
            if (pair != null) {
                if (sc == null) {
                    LogHelper.d("onActivityCreated - build SC for UUID : %s", uuid);
                    mScreenComponents.put(uuid, pair.first);
                }
                LogHelper.d("onActivityCreated - build AC for UUID : %s", uuid);
                mActivityComponents.put(uuid, pair.second);
            }
        }
    }

    @Override
    public final void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (isScoped(activity.getClass())) {
            String uuid = getActivityUUID(activity);
            if (uuid != null) {
                LogHelper.d("onActivitySaveInstanceState - saving UUID : %s", uuid);
                outState.putString(EXTRA_ACTIVITY_ID, uuid);
                onActivitySaved(activity, outState, mScreenComponents.get(uuid));
            }
        }
    }

    @Override
    public final void onActivityDestroyed(Activity activity) {
        if (isScoped(activity.getClass())) {
            String uuid = getActivityUUID(activity);
            if (uuid != null) {
                if (activity.isFinishing()) {
                    LogHelper.d("onActivityDestroyed - removing SC for UUID : %s", uuid);
                    removeUuidMappingFor(activity);
                    mScreenComponents.remove(uuid);
                }
                LogHelper.d("onActivityDestroyed - removing AC for UUID : %s", uuid);
                mActivityComponents.remove(uuid);
            }
        }
    }

    @Override
    public final void onActivityStarted(Activity activity) {
        // do nothing
    }

    @Override
    public final void onActivityResumed(Activity activity) {
        // do nothing
    }

    @Override
    public final void onActivityPaused(Activity activity) {
        // do nothing
    }

    @Override
    public final void onActivityStopped(Activity activity) {
        // do nothing
    }
}
