package eu.inloop.knight.sample.view;

import java.util.List;

import eu.inloop.knight.IView;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.Order;

/**
 * Interface {@link IContactView}
 *
 * @author f3rog
 * @version 2015-07-09
 */
public interface IContactView extends IView {

    void show(String message);

    void show(Contact contact);

    void initOrders(List<Order> contact);

    void refreshOrders();

    void showLoading(boolean show);

    void gotoBack();

}
