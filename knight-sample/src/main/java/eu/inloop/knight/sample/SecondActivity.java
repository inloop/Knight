package eu.inloop.knight.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import eu.inloop.knight.Scoped;

@Scoped
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

}
