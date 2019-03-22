package com.albertech.editpanel.base;

import android.support.v4.app.Fragment;

public interface IFunc extends ITrigger, IDefaultFuncStatus {

    boolean needContainer();

    Fragment content();
}
