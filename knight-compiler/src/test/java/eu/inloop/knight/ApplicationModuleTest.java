package eu.inloop.knight;

import android.app.Application;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.annotation.Generated;
import javax.inject.Singleton;
import javax.tools.JavaFileObject;

import dagger.Module;
import dagger.Provides;
import eu.inloop.knight.scope.AppScope;

/**
 * Class {@link ApplicationModuleTest}
 *
 * @author FrantisekGazo
 * @version 2015-11-19
 */
public class ApplicationModuleTest extends BaseTest {

    @Test
    public void noProvided() {
        JavaFileObject module = JavaFileObjects.forSourceString(KNIGHT_MODULE + "ApplicationModule",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT_MODULE,
                        "",
                        importClass(
                                Application.class,
                                Provides.class,
                                Module.class,
                                Generated.class
                        ),
                        "",
                        GENERATED,
                        "@Module",
                        "public final class ApplicationModule {",
                        "",
                        "   private final Application mApplication;",
                        "",
                        "   public ApplicationModule(Application application) {",
                        "       mApplication = application;",
                        "   }",
                        "",
                        "   @Provides",
                        "   public Application providesApplication() {",
                        "       return mApplication;",
                        "   }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP)
                .compilesWithoutError()
                .and()
                .generatesSources(module);
    }

    @Test
    public void util() {
        JavaFileObject util = JavaFileObjects.forSourceString("com.example.Util",
                Joiner.on('\n').join(
                        "package com.example;",
                        "",
                        importClass(
                                Application.class,
                                AppProvided.class,
                                Singleton.class
                        ),
                        "",
                        "public class Util {",
                        "",
                        "   @AppProvided",
                        "   @Singleton",
                        "   public Util(Application app) {}",
                        "",
                        "}"
                )
        );

        JavaFileObject module = JavaFileObjects.forSourceString(KNIGHT_MODULE + "ApplicationModule",
                Joiner.on('\n').join(
                        PACKAGE_KNIGHT_MODULE,
                        "",
                        importClass(
                                "com.example.Util",
                                Application.class,
                                AppScope.class,
                                Provides.class,
                                Module.class,
                                Generated.class
                        ),
                        "",
                        GENERATED,
                        "@Module",
                        "public final class ApplicationModule {",
                        "",
                        "   private final Application mApplication;",
                        "",
                        "   public ApplicationModule(Application application) {",
                        "       mApplication = application;",
                        "   }",
                        "",
                        "   @Provides",
                        "   public Application providesApplication() {",
                        "       return mApplication;",
                        "   }",
                        "",
                        "   @Provides",
                        "   @AppScope",
                        "   public Util providesUtil(Application app) {",
                        "       return new Util(app);",
                        "   }",
                        "",
                        "}"
                )
        );

        assertFiles(EMPTY_KNIGHT_APP, util)
                .compilesWithoutError()
                .and()
                .generatesSources(module);
    }

}
