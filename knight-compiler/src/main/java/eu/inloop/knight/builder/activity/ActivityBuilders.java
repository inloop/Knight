package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.builder.ComponentBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.scope.ActivityScope;
import eu.inloop.knight.scope.ScreenScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityBuilders}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ActivityBuilders {

    public ClassName activityName;

    public ScreenModuleBuilder SM;
    public ComponentBuilder SC;
    public ScreenComponentFactoryBuilder SCF;

    public ActivityModuleBuilder AM;
    public ComponentBuilder AC;
    public ActivityComponentFactoryBuilder ACF;

    public ActivityBuilders(ClassName activityName) throws ProcessorError {
        this.activityName = activityName;
        // Screen Scope
        SM = new ScreenModuleBuilder(activityName);
        SC = new ComponentBuilder(ScreenScope.class, GCN.SCREEN_COMPONENT, activityName);
        SC.addModule(SM.getClassName());
        SCF = new ScreenComponentFactoryBuilder(SC.getClassName());
        // Activity Scope
        AM = new ActivityModuleBuilder(activityName);
        AC = new ComponentBuilder(ActivityScope.class, GCN.ACTIVITY_COMPONENT, activityName);
        AC.addModule(AM.getClassName());
        ACF = new ActivityComponentFactoryBuilder(AC.getClassName());

        // sub component
        SC.addPlusMethod(AC.getClassName(), AM.getClassName());
        // add inject for Activity
        AC.addInjectMethod(activityName);
    }

    public void buildAll(Filer filer) throws IOException, ProcessorError {
        SM.build(filer);
        SC.build(filer);
        SCF.build(filer);

        AM.build(filer);
        AC.build(filer);
        ACF.build(filer);
    }

    public ClassName[] getScreenModules() {
        return new ClassName[]{SM.getClassName()};
    }
}
