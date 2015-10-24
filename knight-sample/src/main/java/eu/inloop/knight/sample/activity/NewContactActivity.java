package eu.inloop.knight.sample.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import eu.inloop.knight.Scoped;
import eu.inloop.knight.sample.R;

/**
 * Class {@link NewContactActivity}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
@Scoped
public class NewContactActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contact_new;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show back arrow
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) toolbar.setDisplayHomeAsUpEnabled(true);
    }

}
