package eu.inloop.knight.sample.module;

import android.app.Activity;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import eu.inloop.knight.ActivityProvided;
import eu.inloop.knight.sample.activity.ContactListActivity;
import eu.inloop.knight.scope.ActivityScope;

/**
 * Class {@link ActivityModule}
 *
 * @author FrantisekGazo
 * @version 2015-10-24
 */
@ActivityProvided({
        ContactListActivity.class
})
@Module
public class ActivityModule {

    @Provides
    @ActivityScope
    public Picasso providePicasso(Activity activity) {
        return Picasso.with(activity);
    }

}
