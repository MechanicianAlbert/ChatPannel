package com.albertech.editpanel.kernal;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;



/**
 * 用于获取系统输入法尺寸或其他相关尺寸的工具类, 包内可见, 不可继承
 *
 * @author Albert
 * 20181225
 */
final class IpUtil {

    private static final DisplayMetrics DISPLAY_METRICS = new DisplayMetrics();
    private static final Rect VISIABLE_RECT = new Rect();


    private IpUtil() {

    }


    /**
     * 获取系统输入法高度
     *
     * @param v
     * @return
     */
    static int getInputMethodHeight(View v) {
        return getScreenHeight(v.getContext().getApplicationContext()) - getWindowBottomHeight(v);
    }


    /**
     * 获得屏幕高度
     * 此方法会受状态栏和底部虚拟键影响, 需要根据具体机型适配
     *
     * @param context
     * @return
     */
    private static int getScreenHeight(Context context) {
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(DISPLAY_METRICS);
        return DISPLAY_METRICS.heightPixels;
    }

    /**
     * 获得当前屏幕中Activity的底部高度, 当系统输入法弹起时, Activity没有充满屏幕, 其底部高度与屏幕高度之差, 就是输入法的高度;
     * 此方法会受状态栏和底部虚拟键影响, 需要根据具体机型适配
     *
     * @param view
     * @return
     */
    private static int getWindowBottomHeight(View view) {
        view.getWindowVisibleDisplayFrame(VISIABLE_RECT);
        return VISIABLE_RECT.bottom;
    }

}
