package eu.inloop.knight.sample.bus;

import eu.inloop.knight.sample.model.Contact;

/**
 * Class {@link NewContactEvent}.
 *
 * @author f3rog
 * @version 2015-07-10
 */
public class NewContactEvent {

    private Contact mContact;

    public NewContactEvent(Contact contact) {
        mContact = contact;
    }

    public Contact getContact() {
        return mContact;
    }
}
