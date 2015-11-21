package eu.inloop.knight.name;

import com.squareup.javapoet.ClassName;

/**
 * Enum {@link GCN}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public enum GCN {

    KNIGHT("Knight"),
    INJECTOR("Injector"),
    NAVIGATOR("I"),
    APPLICATION_COMPONENT("ApplicationComponent"),
    APPLICATION_MODULE("ApplicationModule"),
    SCREEN_COMPONENT("ScreenComponentFor%s"),
    ACTIVITY_COMPONENT("ActivityComponentFor%s"),
    SCREEN_MODULE("ScreenModuleFor%s"),
    ACTIVITY_MODULE("ActivityModuleFor%s"),
    COMPONENT_FACTORY("ComponentFactoryFor%s"),
    EXTENDED_MODULE("Extended_%s"),
    // Dagger generated class names
    DAGGER("Dagger%s")
    ;

    private String mName;

    GCN(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public String formatName(String arg) {
        if (arg != null) {
            return String.format(mName, arg);
        } else {
            return mName;
        }
    }
    public String formatName(ClassName arg) {
        if (arg != null) {
            return String.format(mName, arg.simpleName());
        } else {
            return mName;
        }
    }
}
