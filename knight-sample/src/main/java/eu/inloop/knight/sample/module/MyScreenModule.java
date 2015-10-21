package eu.inloop.knight.sample.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import eu.inloop.knight.ScreenProvided;
import eu.inloop.knight.sample.MainActivity;
import eu.inloop.knight.scope.ScreenScope;

/**
 * Class {@link MyScreenModule}
 *
 * @author FrantisekGazo
 * @version 2015-10-21
 */
@ScreenProvided(in = MainActivity.class)
@Module
public class MyScreenModule {

    @Named("S")
    @Provides
    @ScreenScope
    public String getS() {
        return "Sssssssssss";
    }

    @Named("T")
    @Provides
    public String getT() {
        return "Ttttttttttt";
    }

}
