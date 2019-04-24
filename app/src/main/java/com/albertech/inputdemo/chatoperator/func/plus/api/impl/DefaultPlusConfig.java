package com.albertech.inputdemo.chatoperator.func.plus.api.impl;


import com.albertech.inputdemo.chatoperator.func.plus.api.IPlusConfig;
import com.albertech.inputdemo.chatoperator.func.plus.api.OnPlusItemClickListener;


public class DefaultPlusConfig implements IPlusConfig, DefaultPlusConstant {

    @Override
    public OnPlusItemClickListener getOnPlusItemClickListener() {
        return null;
    }

    @Override
    public int[][] getPlusRes() {
        return ALL_RES;
    }

    @Override
    public String[][] getPlusName() {
        return ALL_NAME;
    }
}
