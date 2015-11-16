package eu.inloop.knight.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.adapter.BaseRecyclerAdapter;
import eu.inloop.knight.sample.adapter.ContactRecyclerAdapter;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.presenter.ContactListPresenter;
import eu.inloop.knight.sample.view.IContactListView;
import the.knight.I;

/**
 * Class {@link ContactListFragment}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class ContactListFragment extends BaseFragment implements IContactListView {

    private ContactRecyclerAdapter mAdapter;

    @Inject
    ContactListPresenter mPresenter;

    @Bind(R.id.rv_contacts)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_empty)
    TextView mEmpty;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_contact_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        mPresenter.bindView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.releaseView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contact_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_add_contact:
                // Add new contact
                mPresenter.onAddClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void initContacts(List<Contact> contacts) {
        mAdapter = new ContactRecyclerAdapter(getActivity(), contacts);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                mPresenter.onContactClicked(position);
            }
        });
        setVisibility(mEmpty, mAdapter.isEmpty());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void refreshContacts() {
        mAdapter.notifyDataSetChanged();
        setVisibility(mEmpty, mAdapter.isEmpty());
    }

    @Override
    public void showLoading(boolean show) {
        setVisibility(mProgressBar, show);
        setVisibility(mEmpty, !show && mAdapter.isEmpty());
    }

    @Override
    public void show(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoContactDetailView(Contact contact) {
        I.startContactActivity(getActivity(), contact);
    }

    @Override
    public void gotoAddContactView() {
        I.startNewContactActivity(getActivity());
    }

}
