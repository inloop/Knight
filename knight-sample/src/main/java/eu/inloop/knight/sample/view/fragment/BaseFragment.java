package eu.inloop.knight.sample.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Class {@link BaseFragment}
 */
public abstract class BaseFragment extends Fragment {

    abstract protected int getLayoutRes();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    protected void setVisibility(@NonNull View view, boolean show) {
        view.setVisibility((show) ? View.VISIBLE : View.GONE);
    }

}
