package com.albertech.inputdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_def) {
                    to(DefInputPannelActivity.class);
                } else if (view.getId() == R.id.btn_cus) {
                    to(CusInputPannelActivity.class);
                }
            }
        };
        findViewById(R.id.btn_def).setOnClickListener(l);
        findViewById(R.id.btn_cus).setOnClickListener(l);
    }

    private void to(Class<? extends AppCompatActivity> c) {
        startActivity(new Intent(getApplicationContext(), c));
    }


}
