package eu.inloop.knight.sample.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import eu.inloop.knight.BasePresenter;
import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.sample.activity.ContactActivity;
import eu.inloop.knight.sample.activity.NewContactActivity;
import eu.inloop.knight.sample.bus.NewContactEvent;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.ContactError;
import eu.inloop.knight.sample.model.Items;
import eu.inloop.knight.sample.model.Order;
import eu.inloop.knight.sample.model.api.ApiCallback;
import eu.inloop.knight.sample.model.api.ApiError;
import eu.inloop.knight.sample.model.api.IApi;
import eu.inloop.knight.sample.view.IContactView;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Class {@link ContactPresenter}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class ContactPresenter extends BasePresenter<IContactView> {

    private static final int MIN_LENGTH = 5;

    private final IApi mApi;
    private final Contact mContact;
    private final boolean mIsNew;

    private EventBus mEventBus;
    private List<Order> mOrders;
    private boolean mLoading;

    /**
     * Constructor
     */
    @ScreenProvided(in = ContactActivity.class, scoped = true)
    public ContactPresenter(IApi api, Contact contact) {
        this(api, contact, false);
    }

    /**
     * Constructor
     */
    @ScreenProvided(in = NewContactActivity.class, scoped = true)
    public ContactPresenter(IApi api, EventBus eventBus) {
        this(api, null, true);
        mEventBus = eventBus;
    }

    /**
     * Constructor
     */
    private ContactPresenter(IApi api, Contact contact, boolean isNew) {
        mApi = api;
        mContact = contact;
        mIsNew = isNew;
        mOrders = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        if (!mIsNew && mContact != null) {
            downloadOrders();
        }
    }

    @Override
    public void onBindView(@NonNull IContactView view) {
        super.onBindView(view);
        if (!mIsNew) {
            view.show(mContact);
            view.initOrders(mOrders);
            view.showLoading(mLoading);
        }
    }

    public ContactError onSaveClicked(String name, String phone) {
        ContactError error = new ContactError();

        error.setNameError(checkValue(name));
        error.setPhoneError(checkValue(phone));

        if (error.is()) {
            return error;
        } else {
            createContact(new Contact(name, phone));
            return null;
        }
    }

    private void setLoading(boolean loading) {
        mLoading = loading;
        if (getView() != null) getView().showLoading(mLoading);
    }

    /**
     * Checks given value for errors
     */
    private static ContactError.Type checkValue(String value) {
        if (value == null) {
            return ContactError.Type.REQUIRED;
        }
        value = value.trim();
        if (value.isEmpty()) {
            return ContactError.Type.REQUIRED;
        } else if (value.length() < MIN_LENGTH) {
            return ContactError.Type.SHORT;
        } else {
            return ContactError.Type.NONE;
        }
    }

    /**
     * Creates new contact via API
     */
    private void createContact(final Contact contact) {
        setLoading(true);
        mApi.postContact(contact, new ApiCallback<Contact>() {
            @Override
            public void onSuccess(Contact newContact, Response response) {
                setLoading(false);
                mEventBus.postSticky(new NewContactEvent(newContact));
                if (getView() != null) getView().gotoBack();
            }

            @Override
            public void onFailure(ApiError apiError, RetrofitError error) {
                setLoading(false);
                if (getView() != null) {
                    if (apiError != null) {
                        getView().show(apiError.getError().getMessage());
                    }
                }
            }
        });
    }

    /**
     * Gets order list form API. (If offline then from Cache)
     */
    private void downloadOrders() {
        setLoading(true);
        mApi.getOrders(mContact.getId(), new ApiCallback<Items<Order>>() {
            @Override
            public void onSuccess(Items<Order> orderItems, Response response) {
                mOrders.clear();
                mOrders.addAll(orderItems.getItems());
                if (getView() != null) {
                    setLoading(false);
                    getView().refreshOrders();
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

    public void onPhoneClicked() {

    }

}
