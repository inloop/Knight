package eu.inloop.knight;

import android.app.Activity;
import android.os.Bundle;

import eu.inloop.knight.core.IActivityComponent;
import eu.inloop.knight.core.IAppComponent;
import eu.inloop.knight.core.IScreenComponent;

/**
 * Class {@link ComponentFactory}
 *
 * @author FrantisekGazo
 * @version 2015-10-19
 */
public abstract class ComponentFactory<AppC extends IAppComponent, SC extends IScreenComponent, AC extends IActivityComponent> {

    public static class Components<SC extends IScreenComponent, AC extends IActivityComponent> {
        SC sc;
        AC ac;
    }

    public abstract Components<SC, AC> build(AppC appComponent, SC screenComponent, Bundle bundle, Activity activity);
    public abstract SC build(AppC appComponent, Bundle bundle);
    public abstract AC build(SC screenComponent, Activity activity);

}
