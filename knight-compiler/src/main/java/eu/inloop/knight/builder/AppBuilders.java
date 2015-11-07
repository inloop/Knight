package eu.inloop.knight.builder;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.processing.Filer;

import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.component.AppComponentBuilder;
import eu.inloop.knight.builder.module.AppModuleBuilder;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppBuilders}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class AppBuilders {

    public KnightBuilder Knight;
    public InjectorBuilder Injector;
    public NavigatorBuilder Navigator;
    public AppComponentBuilder AppC;
    public ComponentFactoryBuilder AppCF;
    public AppModuleBuilder AppM;

    public AppBuilders() throws ProcessorError {
        Knight = new KnightBuilder();
        Injector = new InjectorBuilder();
        Navigator = new NavigatorBuilder();
        // Application Scope
        AppM = new AppModuleBuilder();
        AppC = new AppComponentBuilder();
        AppC.addModule(AppM.getClassName());
        AppCF = new ComponentFactoryBuilder(EClass.Application.getName());

        Knight.setupAppComponent(AppC.getClassName(), AppCF.getClassName());
    }

    public void buildAll(Filer filer) throws IOException, ProcessorError {
        // create factory methods
        AppCF.addBuildMethod(AppC, AppM);
        // build
        Navigator.build(filer);
        Injector.build(filer);
        Knight.build(filer);
        AppM.build(filer);
        AppC.build(filer);
        AppCF.build(filer);
    }

    public void add(Collection<ActivityBuilders> activityBuildersList) {
        Knight.setupActivities(activityBuildersList);
        for (ActivityBuilders builders : activityBuildersList) {
            AppC.addPlusMethod(builders.SC);
        }
    }
}
