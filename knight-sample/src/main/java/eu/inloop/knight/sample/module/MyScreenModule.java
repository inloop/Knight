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

    // if @ScreenScope is present then cannot annotate with @Provides
    @Named("S")
    @ScreenScope
    public String getString() {
        return "Sssssssssss";
    }

    @Named("T")
    @Provides
    public String getT() {
        return "Ttttttttttt";
    }

}
