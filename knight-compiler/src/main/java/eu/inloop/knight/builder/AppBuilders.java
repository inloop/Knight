package eu.inloop.knight.builder;

import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.view.View;

import com.squareup.javapoet.ClassName;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.processing.Filer;

import eu.inloop.knight.builder.component.AppComponentBuilder;
import eu.inloop.knight.builder.module.AppModuleBuilder;
import eu.inloop.knight.name.EClass;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppBuilders}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class AppBuilders extends BaseScopeBuilders {

    private ClassName mAppName;

    public KnightBuilder Knight;
    public InjectorBuilder Injector;
    public NavigatorBuilder Navigator;
    public AppComponentBuilder AppC;
    public ComponentFactoryBuilder AppCF;
    public AppModuleBuilder AppM;

    public AppBuilders(ClassName appName) throws ProcessorError {
        mAppName = appName;

        Knight = new KnightBuilder(mAppName);
        Injector = new InjectorBuilder();
        Navigator = new NavigatorBuilder();
        // Application Scope
        AppM = new AppModuleBuilder(mAppName);
        AppC = new AppComponentBuilder(mAppName);
        AppC.addModule(AppM.getClassName());
        AppC.addInjectMethod(getAppName());
        AppCF = new ComponentFactoryBuilder(ClassName.get(Application.class));

        Knight.setupAppComponent(AppC.getClassName(), AppCF.getClassName());
        Injector.addInitMethod(Knight, getAppName());
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

    public ClassName getAppName() {
        return mAppName;
    }

    @Override
    protected ClassName[] supportedInjectableClasses() {
        return new ClassName[]{
                ClassName.get(Service.class),
                EClass.SupportFragment.getName(),
                ClassName.get(Fragment.class),
                ClassName.get(View.class)
        };
    }

    @Override
    protected void addInjectMethod(Injectable injectable) {
        AppC.addInjectMethod(injectable.getClassName());
        injectable.setFromApp(getAppName());
    }

}
