package eu.inloop.knight;

import android.support.annotation.NonNull;

/**
 * Interface {@link IPresenter}
 *
 * @author FrantisekGazo
 * @version 2015-10-29
 */
public interface IPresenter<V extends IView> extends IStateful {

    V getView();

    void bindView(@NonNull V view);

    void releaseView();

}

