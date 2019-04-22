package com.albertech.inputdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.albertech.inputdemo.chatoperator.InputPannelView;
import com.albertech.inputdemo.chatoperator.IMsgSender;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        final InputPannelView ipv = findViewById(R.id.ipv);
        final TextView tv = findViewById(R.id.tv);
//        View view = findViewById(R.id.ll);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    ipv.hide();
//                }
//                return false;
//            }
//        });
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

            }
        });

    }


}
