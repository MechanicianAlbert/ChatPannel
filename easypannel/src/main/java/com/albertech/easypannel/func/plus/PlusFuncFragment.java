package com.albertech.easypannel.func.plus;

import android.support.v4.app.Fragment;

import com.albertech.common.base.fragment.BaseFragment;
import com.albertech.editpanel.base.IFunc;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.IFuncStatus;



public abstract class PlusFuncFragment extends BaseFragment implements IFunc, IFuncStatus {

    @Override
    public int triggerId() {
        return R.id.btn_plus;
    }

    @Override
    public int activatedStatus() {
        return FUNC_PLUS;
    }

    @Override
    public int dormantStatus() {
        return FUNC_INPUT;
    }

    @Override
    public int activatedRes() {
        return R.drawable.selector_ip_btn_plus;
    }

    @Override
    public int dormantRes() {
        return R.drawable.selector_ip_btn_plus;
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
