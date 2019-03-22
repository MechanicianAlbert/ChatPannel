package com.albertech.inputdemo.chatoperator.func.emoji;

import android.support.v4.app.Fragment;

import com.albertech.editpanel.base.IFunc;
import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BaseFragment;
import com.albertech.inputdemo.chatoperator.func.IFuncStatus;



public abstract class EmojiFuncFragment extends BaseFragment implements IFunc, IFuncStatus {

    @Override
    public int triggerId() {
        return R.id.btn_emoji;
    }

    @Override
    public int activatedStatus() {
        return FUNC_EMOJI;
    }

    @Override
    public int dormantStatus() {
        return FUNC_INPUT;
    }

    @Override
    public int activatedRes() {
        return R.drawable.selector_ip_btn_emoji;
    }

    @Override
    public int dormantRes() {
        return R.drawable.selector_ip_btn_keyboard;
    }

    @Override
    public boolean needContainer() {
        return true;
    }

    @Override
    public Fragment content() {
        return this;
    }
}
