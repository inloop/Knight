package eu.inloop.knight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class {@link PresenterPool} is used for managing states of non-scoped {@link IPresenter}s.
 *
 * @author FrantisekGazo
 * @version 2015-11-14
 */
public final class PresenterPool implements IStateful {

    private static final String FORMAT_PRESENTER_ID = "%s-%s";
    private Bundle mSavedState = null;
    private Map<String, IPresenter> mPresenters = new HashMap<>();

    public <P extends IPresenter> P add(final P presenter) {
        P p;
        String id = getPresenterId(presenter);
        if (mPresenters.containsKey(id)) {
            // id format makes sure cast will ba ok
            p = (P) mPresenters.get(id);
        } else {
            mPresenters.put(id, presenter);
            p = presenter;
        }
        Bundle savedState = null;
        if (mSavedState != null) savedState = mSavedState.getBundle(id);
        p.onCreate(savedState);
        return p;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mSavedState = savedState;
    }

    @Override
    public void onSave(@NonNull Bundle outState) {
        for (Map.Entry<String, IPresenter> entry : mPresenters.entrySet()) {
            IPresenter presenter = entry.getValue();
            Bundle bundle = new Bundle();
            presenter.onSave(bundle);
            outState.putBundle(getPresenterId(presenter), bundle);
        }
    }

    @Override
    public void onRemove() {
        for (Map.Entry<String, IPresenter> entry : mPresenters.entrySet()) {
            entry.getValue().onRemove();
        }
        mPresenters.clear();
    }

    private String getPresenterId(IPresenter p) {
        return String.format(FORMAT_PRESENTER_ID, p.getClass().getName(), p.getId());
    }
}
