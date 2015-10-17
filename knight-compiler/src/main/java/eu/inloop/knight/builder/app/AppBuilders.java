package eu.inloop.knight.builder.app;

import com.squareup.javapoet.ClassName;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.builder.activity.ActivityBuilders;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppBuilders}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class AppBuilders {

    public KnightBuilder Knight;
    public ApplicationComponentBuilder AppC;
    public ApplicationComponentFactoryBuilder AppCF;
    public ApplicationModuleBuilder AppM;

    public AppBuilders() throws ProcessorError {
        Knight = new KnightBuilder();

        AppM = new ApplicationModuleBuilder();
        AppC = new ApplicationComponentBuilder();
        AppC.addModule(AppM.getClassName());
        AppCF = new ApplicationComponentFactoryBuilder(AppC.getClassName());

        Knight.addFromAppMethod(AppC.getClassName());
    }

    public void buildAll(Filer filer) throws IOException, ProcessorError {
        Knight.build(filer);
        AppM.build(filer);
        AppC.build(filer);
        AppCF.build(filer);
    }

    public void add(ClassName scopedActivity, ActivityBuilders activityBuilders) {
        Knight.addFromMethod(scopedActivity, activityBuilders.AC.getClassName());
        AppC.addPlusMethod(activityBuilders.SC.getClassName(), activityBuilders.getScreenModules());
    }
}
