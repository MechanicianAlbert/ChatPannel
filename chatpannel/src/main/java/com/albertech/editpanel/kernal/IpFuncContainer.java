package com.albertech.editpanel.kernal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * 输入面板中, 装填扩展功能区域的容器, 包内可见, 不可继承
 *
 * @author albert
 * 20181225
 */
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


    /**
     * 覆写测量方法, 在测量前根据系统输入法不同状态的高度, 对原始测量高度进行加工, 达到下述目标效果:
     *      1. 系统输入法弹出时, 扩展功能容器收缩;
     *      2. 系统输入法收起时, 扩展功能根据状态扩张或保持隐藏;
     *      3. 防止状态切换时, 由于系统输入法的收起动画, 而导致的跳闪;
     *
     * @param widthMeasureSpec 宽度的测量规格
     * @param heightMeasureSpec 高度的测量规格
     */
    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取容器高度的原始大小
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 获取容器原始高度与系统输入法高度的差值
        int difference = originalHeight - IpUtil.getInputMethodHeight(this);
        // 容器的处理后高度, 取上述差值与0间的大值, 即上述差值为负时, 使容器高度为0
        int realHeight = Math.max(difference, 0);
        // 使用处理后的高度, 以及原始的高度测量精度, 生成处理后的高度测量规格
        int realHeightMeasureSpec = MeasureSpec.makeMeasureSpec(realHeight, MeasureSpec.getMode(heightMeasureSpec));
        // 让处理后的高度测量规格, 参与后续的系统测量
        super.onMeasure(widthMeasureSpec, realHeightMeasureSpec);
    }


    /**
     * 将本容器的实际高度设置为传入的目标高度
     * @param targetHeight
     */
    void setTargetHeight(int targetHeight) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = targetHeight;
        setLayoutParams(lp);
    }
}
