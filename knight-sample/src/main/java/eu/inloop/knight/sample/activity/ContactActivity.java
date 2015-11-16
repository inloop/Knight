package eu.inloop.knight.sample.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import eu.inloop.knight.KnightActivity;
import eu.inloop.knight.With;
import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.fragment.ContactFragment;
import eu.inloop.knight.sample.model.Contact;

/**
 * Class {@link ContactActivity}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
@KnightActivity(
        with = @With(name = "contact", type = Contact.class),
        injects = {
                ContactFragment.class
        }
)
public class ContactActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contact;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show back arrow
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) toolbar.setDisplayHomeAsUpEnabled(true);
    }

}
