package eu.inloop.knight.builder.app;

import com.squareup.javapoet.ClassName;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.EClass;
import eu.inloop.knight.builder.ComponentBuilder;
import eu.inloop.knight.builder.ComponentFactoryBuilder;
import eu.inloop.knight.builder.GCN;
import eu.inloop.knight.builder.ModuleBuilder;
import eu.inloop.knight.builder.activity.ActivityBuilders;
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
