package eu.inloop.knight.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import eu.inloop.knight.Injectable;
import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.activity.ContactActivity;
import eu.inloop.knight.sample.adapter.OrderRecyclerAdapter;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.Order;
import eu.inloop.knight.sample.presenter.ContactPresenter;
import eu.inloop.knight.sample.view.IContactView;
import the.knight.Knight;

/**
 * Class {@link ContactFragment}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
@Injectable(from = ContactActivity.class)
public class ContactFragment extends BaseFragment implements IContactView {

    private OrderRecyclerAdapter mAdapter;

    @Inject
    ContactPresenter mPresenter;

    @Bind(R.id.tv_contact_phone)
    TextView mPhone;
    @Bind(R.id.rv_orders)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_empty)
    TextView mEmpty;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_contact;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Knight.from((ContactActivity) getActivity()).inject(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPresenter.bindView(this);
    }

    @Override
    public void show(Contact contact) {
        if (contact != null) {
            getActivity().setTitle(contact.getName());
            mPhone.setText(contact.getPhone());
        }
    }

    @Override
    public void initOrders(List<Order> contacts) {
        mAdapter = new OrderRecyclerAdapter(getActivity(), contacts);
        mRecyclerView.setAdapter(mAdapter);
        setVisibility(mEmpty, mAdapter.isEmpty());
    }

    @Override
    public void refreshOrders() {
        mAdapter.notifyDataSetChanged();
        setVisibility(mEmpty, mAdapter.isEmpty());
    }

    @Override
    public void showLoading(boolean show) {
        setVisibility(mProgressBar, show);
        setVisibility(mEmpty, !show && mAdapter.isEmpty());
    }

    @Override
    public void gotoBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void show(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
