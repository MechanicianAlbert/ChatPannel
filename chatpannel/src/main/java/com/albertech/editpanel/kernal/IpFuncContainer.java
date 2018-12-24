package com.albertech.editpanel.kernal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;


final class IpFuncContainer extends FrameLayout {

    public IpFuncContainer(@NonNull Context context) {
        super(context);
    }

    public IpFuncContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IpFuncContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Math.max(MeasureSpec.getSize(heightMeasureSpec) - IpUtil.getInputMethodHeight(this), 0), MeasureSpec.getMode(heightMeasureSpec)));
    }


    final void setTargetHeight(int targetHeight) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = targetHeight;
        setLayoutParams(lp);
    }
}
