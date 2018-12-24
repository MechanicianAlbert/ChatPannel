package com.albertech.inputdemo.chatoperator.func.plus;

import android.support.v4.app.Fragment;

import com.albertech.inputdemo.R;
import com.albertech.editpanel.base.BaseFuncFragment;
import com.albertech.inputdemo.chatoperator.func.IFuncStatus;

public class PlusIFunc extends BaseFuncFragment  implements IFuncStatus {

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
        return FUNC_HIDE;
    }

    @Override
    public int activatedRes() {
        return R.drawable.ip_btn_plus;
    }

    @Override
    public int dormantRes() {
        return R.drawable.ip_btn_hide;
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
