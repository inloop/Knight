package eu.inloop.knight.sample;

import dagger.Module;
import eu.inloop.knight.ScreenProvided;

/**
 * Class {@link ExtraScreenModule}
 *
 * @author FrantisekGazo
 * @version 2015-10-18
 */
@ScreenProvided(in = SecondActivity.class)
@Module
public class ExtraScreenModule {
}
