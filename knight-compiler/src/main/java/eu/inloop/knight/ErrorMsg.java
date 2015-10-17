package eu.inloop.knight;

/**
 * Enum {@link ErrorMsg}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public enum ErrorMsg {

    Scoped_can_be_only_Activity("Only Activity class can be annotated with @%s.", Scoped.class.getSimpleName()),
    Injectable_can_contain_only_Scoped_Activities("@%s can contain only Activity classes annotated with @%s.", Injectable.class.getSimpleName(), Scoped.class.getSimpleName()),
    ;

    private String mMessage;

    ErrorMsg(String msgFormat, Object... args) {
        this.mMessage = String.format(msgFormat, args);
    }

    @Override
    public String toString() {
        return mMessage;
    }
}
