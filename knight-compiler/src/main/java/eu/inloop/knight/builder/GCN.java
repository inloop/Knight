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
    SCREEN_COMPONENT("%s_ScreenComponent"),
    ACTIVITY_COMPONENT("%s_ActivityComponent"),
    SCREEN_MODULE("%s_ScreenModule"),
    ACTIVITY_MODULE("%s_ActivityModule"),
    COMPONENT_FACTORY("%s_ComponentFactory"),
    ;

    private String mName;

    GCN(String mName) {
        this.mName = mName;
    }

    public String getmName() {
        return mName;
    }
}
