package com.albertech.easypannel.func.voice;

import android.support.v4.app.Fragment;

import com.albertech.easypannel.R;
import com.albertech.editpanel.base.BaseFuncFragment;
import com.albertech.easypannel.func.IFuncStatus;

public class VoiceIFunc extends BaseFuncFragment  implements IFuncStatus {

    @Override
    public int triggerId() {
        return R.id.btn_voice;
    }

    @Override
    public int activatedStatus() {
        return FUNC_VOICE;
    }

    @Override
    public int dormantStatus() {
        return FUNC_INPUT;
    }

    @Override
    public int activatedRes() {
        return R.drawable.selector_ip_btn_voice;
    }

    @Override
    public int dormantRes() {
        return R.drawable.selector_ip_btn_keyboard;
    }

    @Override
    public boolean needContainer() {
        return false;
    }

    @Override
    public Fragment content() {
        return this;
    }
}
