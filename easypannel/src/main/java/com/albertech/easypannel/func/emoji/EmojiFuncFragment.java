package com.albertech.easypannel.func.emoji;

import android.support.v4.app.Fragment;

import com.albertech.common.base.fragment.BaseFragment;
import com.albertech.editpanel.base.IFunc;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.IFuncStatus;



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
