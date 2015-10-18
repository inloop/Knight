package eu.inloop.knight;

import dagger.Module;

/**
 * Enum {@link ErrorMsg}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public enum ErrorMsg {

    Scoped_can_be_only_Activity("Only Activity class can be annotated with @%s.", Scoped.class.getSimpleName()),
    Injectable_can_contain_only_Scoped_Activities("@%s can contain only Activity classes annotated with @%s.", Injectable.class.getSimpleName(), Scoped.class.getSimpleName()),
    Provided_can_contain_only_Scoped_Activities("@Provided can contain only Activity classes annotated with @%s.", Scoped.class.getSimpleName()),
    Provided_constructor_has_to_be_public("@Provided constructor has to be public."),
    Provided_method_has_to_be_public_static("@Provided method has to be public static."),
    Missing_Module_annotation("Provided class has to be annotated with @%s.", Module.class.getSimpleName()),
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
