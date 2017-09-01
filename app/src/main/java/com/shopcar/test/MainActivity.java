package com.shopcar.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SnowFallView2 snowFallView2 = (SnowFallView2) findViewById(R.id.snow2);
        snowFallView2.postDelayed(new Runnable() {
            @Override
            public void run() {
                snowFallView2.startFall();
            }
        },200);
    }
}
