package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.TypeElement;


/**
 * Class {@link Injectable}
 *
 * @author FrantisekGazo
 * @version 2015-11-16
 */
public class Injectable {

    private TypeElement mElement;
    private ClassName mClassName;
    private Set<ClassName> mFromActivities;
    private ClassName mFromApp;

    public Injectable(TypeElement e) {
        mElement = e;
        mClassName = ClassName.get(e);
        mFromActivities = new HashSet<>();
        mFromApp = null;
    }

    public TypeElement getElement() {
        return mElement;
    }

    public Set<ClassName> getFromActivities() {
        return mFromActivities;
    }

    public ClassName getFromApp() {
        return mFromApp;
    }

    public ClassName getClassName() {
        return mClassName;
    }

    public void addFromActivity(ClassName activityClassName) {
        mFromActivities.add(activityClassName);
    }

    public void setFromApp(ClassName appClassName) {
        mFromApp = appClassName;
    }

    public boolean isValid() {
        return mFromApp != null || !mFromActivities.isEmpty();
    }
}
