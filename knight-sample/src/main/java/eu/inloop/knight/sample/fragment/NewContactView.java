package eu.inloop.knight.sample.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.ContactError;
import eu.inloop.knight.sample.model.Order;
import eu.inloop.knight.sample.presenter.AContactPresenter;
import eu.inloop.knight.sample.view.IContactView;

/**
 * Class {@link NewContactView}
 *
 * @author FrantisekGazo
 * @version 2015-11-13
 */
public class NewContactView
        extends LinearLayout
        implements IContactView {

    @Inject
    AContactPresenter mPresenter;

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

    public NewContactView(Context context) {
        super(context);
    }

    public NewContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NewContactView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenter.bindView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenter.releaseView();
    }

    @Override
    public void gotoBack() {
        ((Activity) getContext()).onBackPressed();
    }

    @Override
    public void show(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
                editText.setError(getContext().getString(R.string.err_required));
                break;
            case SHORT:
                editText.setError(getContext().getString(R.string.err_too_short));
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
