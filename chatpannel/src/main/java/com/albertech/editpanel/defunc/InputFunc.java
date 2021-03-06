package com.albertech.editpanel.defunc;

import android.support.v4.app.Fragment;

import com.albertech.editpanel.base.BaseFuncFragment;

public class InputFunc extends BaseFuncFragment {

    @Override
    public int triggerId() {
        return 0;
    }

    @Override
    public int activatedStatus() {
        return FUNC_INPUT;
    }

    @Override
    public int dormantStatus() {
        return FUNC_HIDE;
    }

    @Override
    public int activatedRes() {
        return 0;
    }

    @Override
    public int dormantRes() {
        return 0;
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
