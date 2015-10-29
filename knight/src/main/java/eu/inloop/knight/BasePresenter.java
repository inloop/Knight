package eu.inloop.knight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Class {@link BasePresenter}
 *
 * @author FrantisekGazo
 * @version 2015-10-21
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    private V mView;

    @Override
    public void onCreate(@Nullable Bundle savedState) {
    }

    @Override
    public void onSave(@NonNull Bundle outState) {
    }

    @Override
    public void onRemove() {
    }

    public void onBindView(@NonNull V view) {
    }

    public void onReleaseView() {
    }

    @Override
    public final void bindView(@NonNull V view) {
        mView = view;
        onBindView(view);
    }

    @Override
    public final void releaseView() {
        mView = null;
        onReleaseView();
    }

    @Override
    public V getView() {
        return mView;
    }

}
