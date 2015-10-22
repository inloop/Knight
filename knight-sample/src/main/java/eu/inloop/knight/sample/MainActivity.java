package eu.inloop.knight.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inloop.knight.Scoped;
import eu.inloop.knight.With;
import eu.inloop.knight.sample.util.StringUtil;
import the.knight.I;

@Scoped({
        @With(name = "flag", type = boolean.class),
        @With(name = "number", type = Double.class)
})
public class MainActivity extends AppCompatActivity {

    @Named("S")
    @Inject
    String s;
    @Inject
    int injectedNumber;
    @Inject
    StringUtil stringUtil;

    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.text2)
    TextView text2;

    @OnClick(R.id.btn_new)
    public void gotoNew() {
        I.startMainActivity(this, false, null);
    }

    @OnClick(R.id.btn_second)
    public void gotoSecond() {
        startActivity(I.forSecondActivity(this));
    }

    @OnClick(R.id.btn_unscoped)
    public void gotoUnscoped() {
        startActivity(new Intent(this, UnScopedActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        text1.setText(String.valueOf(injectedNumber));
        text2.setText(stringUtil.doSomething());
        text2.append("\n");
        text2.append(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
