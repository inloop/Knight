package eu.inloop.knight.sample.presenter;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import eu.inloop.knight.As;
import eu.inloop.knight.BasePresenter;
import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.bus.ContactListEvent;
import eu.inloop.knight.sample.bus.NewContactEvent;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.Items;
import eu.inloop.knight.sample.model.api.ApiCallback;
import eu.inloop.knight.sample.model.api.ApiError;
import eu.inloop.knight.sample.model.api.IApi;
import eu.inloop.knight.sample.util.NetUtils;
import eu.inloop.knight.sample.view.IContactListView;
import eu.inloop.knight.sample.view.activity.ContactListActivity;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Class {@link ContactListPresenter}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class ContactListPresenter
        extends BasePresenter<IContactListView>
        implements IContactListPresenter {

    private final Application mApp;
    private final NetUtils mNetUtils;
    private final IApi mApi;
    private final EventBus mEventBus;
    private List<Contact> mContacts;
    private boolean mLoading;

    /**
     * Constructor
     */
    @ScreenProvided(ContactListActivity.class)
    @Singleton
    @As(IContactListPresenter.class)
    public ContactListPresenter(Application app, NetUtils utils, IApi api, EventBus eventBus) {
        mApp = app;
        mNetUtils = utils;
        mApi = api;
        mEventBus = eventBus;
        mContacts = new ArrayList<>();

        mEventBus.registerSticky(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        mEventBus.unregister(this);
    }

    @Override
    public void onAddClicked() {
        if (getView() != null) getView().gotoAddContactView();
    }

    @Override
    public void onContactClicked(int position) {
        // just to make sure
        if (position < 0 || position >= mContacts.size()) {
            if (getView() != null) getView().refreshContacts();
            return;
        }

        if (getView() != null) getView().gotoContactDetailView(mContacts.get(position));
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        downloadContacts();
    }

    @Override
    public void onBindView(@NonNull IContactListView view) {
        super.onBindView(view);

        view.initContacts(mContacts);
        view.showLoading(mLoading);

        if (!mNetUtils.isNetworkAvailable()) {
            view.show(mApp.getString(R.string.err_no_connection));
        }
    }

    private void setLoading(boolean loading) {
        mLoading = loading;
        if (getView() != null) getView().showLoading(mLoading);
    }

    /**
     * Gets contact list form API. (If offline then from Cache)
     */
    private void downloadContacts() {
        setLoading(true);
        mApi.getContacts(new ApiCallback<Items<Contact>>() {
            @Override
            public void onSuccess(Items<Contact> contactItems, Response response) {
                mContacts.clear();
                mContacts.addAll(contactItems.getItems());
                Collections.sort(mContacts);
                if (getView() != null) {
                    setLoading(false);
                    getView().refreshContacts();
                }
            }

            @Override
            public void onFailure(ApiError apiError, RetrofitError error) {
                if (getView() != null) {
                    setLoading(false);
                    if (apiError != null) {
                        getView().show(apiError.getError().getMessage());
                    }
                }
            }
        });
    }

    //region EventBus Events

    public void onEventBackgroundThread(NewContactEvent event) {
        mContacts.add(event.getContact());
        Collections.sort(mContacts);
        mEventBus.post(ContactListEvent.REFRESH);
        mEventBus.removeStickyEvent(NewContactEvent.class);
    }

    public void onEventMainThread(ContactListEvent event) {
        if (event == ContactListEvent.REFRESH && getView() != null) {
            getView().refreshContacts();
        }
    }

    //endregion

    @Override
    public String getId() {
        return null;
    }
}
