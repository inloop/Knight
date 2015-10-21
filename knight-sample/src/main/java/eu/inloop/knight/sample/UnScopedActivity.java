package eu.inloop.knight.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class UnScopedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unscoped);
    }

}
