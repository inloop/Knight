package eu.inloop.knight.name;

/**
 * Enum {@link GPN}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public enum GPN {

    KNIGHT("the.knight"),
    DI("di"),
    COMPONENTS("component"),
    MODULES("module"),
    FACTORIES("factory"),
    ;

    private String mName;

    GPN(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public static String toString(GPN... mGenPackageName) {
        StringBuilder pckg = new StringBuilder();
        for (int i = 0; i < mGenPackageName.length; i++) {
            if (i > 0) {
                pckg.append(".");
            }
            pckg.append(mGenPackageName[i].getName());
        }
        return pckg.toString();
    }

}
