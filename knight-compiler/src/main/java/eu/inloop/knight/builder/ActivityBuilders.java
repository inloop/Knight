package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;

import java.io.IOException;

import javax.annotation.processing.Filer;

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

    public ModuleBuilder SM;
    public ComponentBuilder SC;

    public ModuleBuilder AM;
    public ComponentBuilder AC;

    public ComponentFactoryBuilder CF;

    public ActivityBuilders(ClassName activityName) throws ProcessorError {
        this.activityName = activityName;
        // Screen Scope
        SM = new ModuleBuilder(ScreenScope.class, GCN.SCREEN_MODULE, activityName);
        SC = new ComponentBuilder(ScreenScope.class, GCN.SCREEN_COMPONENT, activityName);
        SC.addModule(SM.getClassName());
        // Activity Scope
        AM = new ModuleBuilder(ActivityScope.class, GCN.ACTIVITY_MODULE, activityName);
        AC = new ComponentBuilder(ActivityScope.class, GCN.ACTIVITY_COMPONENT, activityName);
        AC.addModule(AM.getClassName());

        CF = new ComponentFactoryBuilder(activityName);
    }

    public void buildAll(Filer filer) throws IOException, ProcessorError {
        // sub component
        SC.addPlusMethod(AC);
        // add inject for Activity
        AC.addInjectMethod(activityName);

        // build
        SM.build(filer);
        SC.build(filer);

        AM.build(filer);
        AC.build(filer);

        CF.build(filer);
    }

    public ClassName[] getScreenModules() {
        return new ClassName[]{SM.getClassName()};
    }

    public ClassName getActivityName() {
        return activityName;
    }
}
