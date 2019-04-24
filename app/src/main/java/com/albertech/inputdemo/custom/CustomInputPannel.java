package com.albertech.inputdemo.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.albertech.easypannel.InputPannelView;
import com.albertech.easypannel.func.plus.PlusFunc;
import com.albertech.editpanel.base.IFunc;

import java.util.Set;

public class CustomInputPannel extends InputPannelView {


    public CustomInputPannel(@NonNull Context context) {
        super(context);
    }

    public CustomInputPannel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomInputPannel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onRegisterFunc(Set<IFunc> funcs) {
        super.onRegisterFunc(funcs);
        funcs.add(PlusFunc.newInstance(new CustomPlusConfig()));
    }

    @Override
    protected boolean useDefaultPlus() {
        return false;
    }
}
