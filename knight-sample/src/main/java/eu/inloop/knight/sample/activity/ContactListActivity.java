package eu.inloop.knight.sample.activity;

import eu.inloop.knight.Scoped;
import eu.inloop.knight.sample.R;

@Scoped
public class ContactListActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contact_list;
    }

}
