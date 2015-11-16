package eu.inloop.knight.sample.activity;

import eu.inloop.knight.KnightActivity;
import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.fragment.ContactListFragment;

@KnightActivity(
        injects = ContactListFragment.class
)
public class ContactListActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contact_list;
    }

}
