package eu.inloop.knight;

import dagger.Module;
import dagger.Provides;
import eu.inloop.knight.scope.ScreenScope;

/**
 * Enum {@link ErrorMsg}
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public enum ErrorMsg {

    Scoped_invalid("Only Activity class can be annotated with @%s.", KnightActivity.class.getSimpleName()),
    Provided_outside_Scoped_Activity("@Provided can contain only Activity classes annotated with @%s.", KnightActivity.class.getSimpleName()),
    Provided_constructor_not_public("@Provided constructor has to be public."),
    Provided_method_not_public_static("@Provided method has to be public static."),
    Missing_Module_annotation("Provided class has to be annotated with @%s.", Module.class.getSimpleName()),
    Module_not_public("@%s has to be public class.", Module.class.getSimpleName()),
    Module_is_abstract("@%s cannot be abstract class.", Module.class.getSimpleName()),
    Screen_Module_is_final("@%s Module cannot be final class if contains @%s methods.", ScreenProvided.class.getSimpleName(), ScreenScope.class.getSimpleName()),
    Screen_Module_without_empty_constructor("@%s Module has to have empty constructor.", ScreenProvided.class.getSimpleName()),
    Screen_Scoped_module_method_with_Provides("@%s Module's method annotated with @%s cannot be also annotated with @%s.", ScreenProvided.class.getSimpleName(), ScreenScope.class.getSimpleName(), Provides.class.getSimpleName()),
    Extra_name_not_unique("@%s must have unique name inside @%s.", Extra.class.getSimpleName(), KnightActivity.class.getSimpleName()),
    Extra_invalid_name("@%s must have valid Java syntax name.", Extra.class.getSimpleName()),
    Invalid_Knight_App("@%s can be used only on public Application class.", KnightApp.class.getSimpleName()),
    More_Knight_Apps("@%s can be only one.", KnightApp.class.getSimpleName()),
    Missing_Knight_App("No @%s was found", KnightApp.class.getSimpleName()),
    Not_injectable("This class is not injected from @%s nor @%s.", KnightApp.class.getSimpleName(), KnightActivity.class.getSimpleName()),
    Invalid_injectable("This class is not supported for injection."),
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
