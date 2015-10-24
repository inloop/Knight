package eu.inloop.knight.sample.view;

import java.util.List;

import eu.inloop.knight.IView;
import eu.inloop.knight.sample.model.Contact;

/**
 * Interface {@link IContactListView}
 *
 * @author f3rog
 * @version 2015-07-09
 */
public interface IContactListView extends IView {

    void show(String message);

    void initContacts(List<Contact> contacts);

    void refreshContacts();

    void showLoading(boolean show);

    void gotoContactDetailView(Contact contact);

    void gotoAddContactView();

}
