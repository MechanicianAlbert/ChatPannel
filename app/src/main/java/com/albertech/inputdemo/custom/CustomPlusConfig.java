package com.albertech.inputdemo.custom;

import android.util.Log;

import com.albertech.easypannel.func.plus.api.IPlusConfig;
import com.albertech.easypannel.func.plus.api.OnPlusItemClickListener;

public class CustomPlusConfig implements OnPlusItemClickListener, IPlusConfig, CustomPlusConstant {

    @Override
    public OnPlusItemClickListener getOnPlusItemClickListener() {
        return this;
    }

    @Override
    public int[][] getPlusRes() {
        return ALL_RES;
    }

    @Override
    public String[][] getPlusName() {
        return ALL_NAME;
    }

    @Override
    public void onPlusItemClick(String name) {
        Log.e("AAA", "Plus name: " + name);
    }
}
