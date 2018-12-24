package com.albertech.inputdemo.chatoperator.func.album;

import android.support.v4.app.Fragment;

import com.albertech.editpanel.base.BaseFuncFragment;
import com.albertech.inputdemo.chatoperator.func.IFuncStatus;

public class AlbumIFunc extends BaseFuncFragment  implements IFuncStatus {

    @Override
    public int triggerId() {
        return 0;
    }

    @Override
    public int activatedStatus() {
        return FUNC_ALBUM;
    }

    @Override
    public int dormantStatus() {
        return FUNC_INPUT;
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
