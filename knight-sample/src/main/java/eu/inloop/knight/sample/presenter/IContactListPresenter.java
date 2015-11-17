package eu.inloop.knight.sample.presenter;

import eu.inloop.knight.IPresenter;
import eu.inloop.knight.sample.view.IContactListView;

/**
 * Interface {@link IContactListPresenter}
 *
 * @author FrantisekGazo
 * @version 2015-11-17
 */
public interface IContactListPresenter extends IPresenter<IContactListView> {

    void onAddClicked();

    void onContactClicked(int position);

}
