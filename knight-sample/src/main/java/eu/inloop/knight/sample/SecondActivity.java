package eu.inloop.knight.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.inloop.knight.Scoped;
import eu.inloop.knight.With;
import eu.inloop.knight.sample.test.User;

@Scoped({
        @With(name = "someone", type = String.class)
})
public class SecondActivity extends AppCompatActivity {

    @Inject
    User user;

    @Bind(R.id.text1)
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        text1.setText(user.toString());
    }

}
