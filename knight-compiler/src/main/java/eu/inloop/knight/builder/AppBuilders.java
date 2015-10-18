package eu.inloop.knight.builder;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.EClass;
import eu.inloop.knight.scope.AppScope;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppBuilders}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class AppBuilders {

    public KnightBuilder Knight;
    public ComponentBuilder AppC;
    public ComponentFactoryBuilder AppCF;
    public ModuleBuilder AppM;

    public AppBuilders() throws ProcessorError {
        Knight = new KnightBuilder();
        // Application Scope
        AppM = new ModuleBuilder(AppScope.class, GCN.APPLICATION_MODULE);
        AppC = new ComponentBuilder(AppScope.class, GCN.APPLICATION_COMPONENT);
        AppC.addModule(AppM.getClassName());
        AppCF = new ComponentFactoryBuilder(EClass.Application.getName());

        Knight.addFromAppMethod(AppC.getClassName());
    }

    public void buildAll(Filer filer) throws IOException, ProcessorError {
        // create factory methods
        AppCF.addBuildMethod(AppC, AppM);
        // build
        Knight.build(filer);
        AppM.build(filer);
        AppC.build(filer);
        AppCF.build(filer);
    }

    public void add(ActivityBuilders activityBuilders) {
        Knight.addFromMethod(activityBuilders);
        AppC.addPlusMethod(activityBuilders.SC);
    }
}
