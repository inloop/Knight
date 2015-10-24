package eu.inloop.knight.sample.model;

/**
 * Class {@link ContactError}.
 *
 * @author f3rog
 * @version 2015-07-10
 */
public class ContactError {

    public enum Type {
        NONE, REQUIRED, SHORT
    }

    private Type mNameError;
    private Type mPhoneError;

    public ContactError() {
        mNameError = Type.NONE;
        mPhoneError = Type.NONE;
    }

    public void setNameError(Type nameError) {
        mNameError = nameError;
    }

    public void setPhoneError(Type phoneError) {
        mPhoneError = phoneError;
    }

    public Type getNameError() {
        return mNameError;
    }

    public Type getPhoneError() {
        return mPhoneError;
    }

    public boolean is() {
        return mNameError != Type.NONE || mPhoneError != Type.NONE;
    }

}
