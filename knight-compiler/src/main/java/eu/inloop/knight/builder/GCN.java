package eu.inloop.knight.builder;

/**
 * Enum {@link GCN}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public enum GCN {

    KNIGHT("Knight"),
    APPLICATION_COMPONENT("ApplicationComponent"),
    APPLICATION_MODULE("ApplicationModule"),
    SCREEN_COMPONENT("ScreenComponentFor%s"),
    ACTIVITY_COMPONENT("ActivityComponentFor%s"),
    SCREEN_MODULE("ScreenModuleFor%s"),
    ACTIVITY_MODULE("ActivityModuleFor%s"),
    COMPONENT_FACTORY("ComponentFactoryFor%s"),
    EXTENDED_MODULE("Extended_%s"),
    ;

    private String mName;

    GCN(String mName) {
        this.mName = mName;
    }

    public String getmName() {
        return mName;
    }
}
