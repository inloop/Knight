package eu.inloop.knight;

import android.support.annotation.NonNull;

/**
 * Interface {@link IPresenter}
 *
 * @author FrantisekGazo
 * @version 2015-10-29
 */
public interface IPresenter<V extends IView> extends IStateful {

    /**
     * @return Currently binded <code>view</code> to this presenter on <code>null</code>.
     */
    V getView();

    /**
     * Binds given <code>view</code> to this presenter.
     *
     * @param view View
     */
    void bindView(@NonNull V view);

    /**
     * Releases current <code>view</code> from this presenter.
     */
    void releaseView();

    /**
     * This method is used in {@link PresenterPool}.
     *
     * @return Presenter ID
     */
    String getId();

}

