package eu.inloop.knight.sample.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import eu.inloop.knight.Injectable;
import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.activity.NewContactActivity;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.ContactError;
import eu.inloop.knight.sample.model.Order;
import eu.inloop.knight.sample.presenter.ContactPresenter;
import eu.inloop.knight.sample.view.IContactView;
import the.knight.Knight;

/**
 * Class {@link NewContactFragment}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
@Injectable(from = NewContactActivity.class)
public class NewContactFragment extends BaseFragment implements IContactView {

    @Inject
    ContactPresenter mPresenter;

    @Bind(R.id.et_contact_name)
    EditText mName;
    @Bind(R.id.et_contact_phone)
    EditText mPhone;

    @OnClick(R.id.btn_save_contact)
    public void save() {
        ContactError error = mPresenter.onSaveClicked(
                mName.getText().toString(),
                mPhone.getText().toString()
        );
        // show error if some occurred
        if (error != null) {
            showError(mName, error.getNameError());
            showError(mPhone, error.getPhoneError());
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_contact_new;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Knight.fromNewContactActivity(getActivity()).inject(this);

        mPresenter.bindView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.releaseView();
    }

    @Override
    public void gotoBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void show(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows given error type for given field.
     *
     * @param editText  Input field
     * @param errorType Error type
     */
    private void showError(EditText editText, ContactError.Type errorType) {
        switch (errorType) {
            case REQUIRED:
                editText.setError(getString(R.string.err_required));
                break;
            case SHORT:
                editText.setError(getString(R.string.err_too_short));
                break;
        }
    }

    //region Not used methods

    @Override
    public void show(Contact contact) {
        // do nothing
    }

    @Override
    public void initOrders(List<Order> contact) {
        // do nothing
    }

    @Override
    public void refreshOrders() {
        // do nothing
    }

    @Override
    public void showLoading(boolean show) {
        // do nothing
    }

    //endregion
}
