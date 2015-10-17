package eu.inloop.knight.builder.activity;

import com.squareup.javapoet.ClassName;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ActivityBuilders}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ActivityBuilders {

    public ClassName className;

    public ScreenModuleBuilder SM;
    public ScreenComponentBuilder SC;
    public ScreenComponentFactoryBuilder SCF;

    public ActivityModuleBuilder AM;
    public ActivityComponentBuilder AC;
    public ActivityComponentFactoryBuilder ACF;

    public ActivityBuilders(ClassName className) throws ProcessorError {
        this.className = className;

        SM = new ScreenModuleBuilder(className);
        SC = new ScreenComponentBuilder(className);
        SC.addModule(SM.getClassName());
        SCF = new ScreenComponentFactoryBuilder(SC.getClassName());

        AM = new ActivityModuleBuilder(className);
        AC = new ActivityComponentBuilder(className);
        AC.addModule(AM.getClassName());
        ACF = new ActivityComponentFactoryBuilder(AC.getClassName());

        SC.addPlusMethod(AC.getClassName(), AM.getClassName());
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
