package eu.inloop.knight.sample.presenter;

import eu.inloop.knight.BasePresenter;
import eu.inloop.knight.sample.model.ContactError;
import eu.inloop.knight.sample.view.IContactView;

/**
 * Class {@link AContactPresenter}
 *
 * @author FrantisekGazo
 * @version 2015-11-17
 */
public abstract class AContactPresenter extends BasePresenter<IContactView> {

    public abstract ContactError onSaveClicked(String name, String phone);

}
