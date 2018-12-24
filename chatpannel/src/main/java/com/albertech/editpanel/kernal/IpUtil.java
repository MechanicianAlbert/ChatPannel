package com.albertech.editpanel.kernal;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

final class IpUtil {

    private static final DisplayMetrics DISPLAY_METRICS = new DisplayMetrics();
    private static final Rect VISIABLE_RECT = new Rect();


    private IpUtil() {

    }


    static int getInputMethodHeight(View v) {
        return getScreenHeight(v.getContext().getApplicationContext()) - getWindowBottomHeight(v);
    }


    private static int getScreenHeight(Context context) {
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(DISPLAY_METRICS);
        return DISPLAY_METRICS.heightPixels;
    }

    private static int getWindowBottomHeight(View view) {
        view.getWindowVisibleDisplayFrame(VISIABLE_RECT);
        return VISIABLE_RECT.bottom;
    }

}
