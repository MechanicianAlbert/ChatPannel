package com.albertech.inputdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.albertech.easypannel.IMsgSender;
import com.albertech.easypannel.InputPannelView;


public class DefInputPannelActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_def);
        init();
    }

    private void init() {
        final InputPannelView ipv = findViewById(R.id.ipv);
        final TextView tv = findViewById(R.id.tv);

        findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipv.hide();
            }
        });

        ipv.setMsgSender(new IMsgSender() {
            @Override
            public void onTextSubmit(Editable text) {
                tv.setText(text);
            }

            @Override
            public void onVoiceSubmit(String path) {
                Log.e("AAA", path);
            }

            @Override
            public void onImageSubmit(String... paths) {
                if (paths != null && paths.length > 0) {
                    for (int i = 0; i < paths.length; i++) {
                        Log.e("AAA", paths[i]);
                    }
                }
            }
        });

    }


}
