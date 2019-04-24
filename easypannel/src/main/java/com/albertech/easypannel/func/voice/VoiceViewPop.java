package com.albertech.easypannel.func.voice;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.albertech.easypannel.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 短语音录制过程中的提示
 */
public class VoiceViewPop implements IVoiceMsgContract.IVoiceView {

    /**
     * 音量指示器图片资源
     */
    private static final int[] VOLUME_INDICATORS = new int[]{
            R.drawable.nt_img_voice_indicator_1,
            R.drawable.nt_img_voice_indicator_2,
            R.drawable.nt_img_voice_indicator_3,
            R.drawable.nt_img_voice_indicator_4,
            R.drawable.nt_img_voice_indicator_5,
            R.drawable.nt_img_voice_indicator_6,
    };

    /**
     * 刷新速率损失, 配合刷新计数器, 用于刷新速率调节
     *      设此值为 v, 刷新计数器对 v 取模, 为 0 时执行刷新, 速率减小 v 倍
     */
    private static final int SPEED_LOSS = 2;

    /**
     * 刷新计数器, 配合刷新速率损失, 用于刷新速率调节
     *      计数器值应使用 %= 取模并且重置自身值, 以免持续增加导致溢出
     */
    private int mVolumnStep;


    // 父窗体
    private final View mRootView;
    // 根布局
    private final PopupWindow mWindow;

    // 音量指示器
    private ImageView mIvIndicator;
    // 倒计时
    private TextView mTvAntiTimer;
    // 文字提示
    private TextView mTvRemind;


    public VoiceViewPop(Context context) {
        mRootView = View.inflate(context, R.layout.nt_pop_voice_remind, null);
        mWindow = new PopupWindow(mRootView, WRAP_CONTENT, WRAP_CONTENT);

        initView();
    }

    private void initView() {
        mIvIndicator = mRootView.findViewById(R.id.iv_voice_indicator);
        mTvAntiTimer = mRootView.findViewById(R.id.tv_anti_timer);
        mTvRemind = mRootView.findViewById(R.id.tv_voice_remind);
    }

    /**
     * 显示浮窗
     * @param view
     */
    private void show(View view) {
        if (mWindow != null) {
            mWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
        // 将提示语背景状态重置
        mTvRemind.setSelected(false);
    }

    /**
     * 隐藏浮窗
     */
    private void hide() {
        if (mWindow != null) {
            mWindow.dismiss();
        }
    }

    /**
     * 显示音量指示器
     */
    private void showIndicator() {
        mIvIndicator.setVisibility(View.VISIBLE);
        mTvAntiTimer.setVisibility(View.GONE);
    }

    /**
     * 显示倒计时
     */
    private void showAntiTimer() {
        mIvIndicator.setVisibility(View.GONE);
        mTvAntiTimer.setVisibility(View.VISIBLE);
    }

    /**
     * 通过音量计算对应的指示器索引
     * @param volume 输入音量值
     * @return 指示器索引
     */
    private int getIndicator(int volume) {
        try{
            int index = Math.min(Math.abs(volume / 2000), VOLUME_INDICATORS.length - 1);
            return VOLUME_INDICATORS[index];
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void onRecordStart() {
        show(mRootView);
        showIndicator();
    }

    @Override
    public void onRecordStop() {
        hide();
    }

    @Override
    public void onTouchPositiveArea() {
        mTvRemind.setText(R.string.str_hint_up_to_cancel);
        mTvRemind.setSelected(false);
    }

    @Override
    public void onTouchNegativeArea() {
        mTvRemind.setText(R.string.str_hint_release_to_cancel);
        mTvRemind.setSelected(true);
    }

    @Override
    public void onVolume(int value) {
        // 计数器累加
        mVolumnStep += 1;
        // 计数器对速率损失取模并重置自身的值
        mVolumnStep %= SPEED_LOSS;
        if (mVolumnStep == 0) {
            // 如果计数满足条件, 刷新音量指示器
            mIvIndicator.setImageResource(getIndicator(value));
        }
    }

    @Override
    public void onTimeUpdate(int second) {
        if (IVoiceMsgContract.MAX_DURATION - second < 10) {
            // 录制时长距最大时长少于 10s, 显示倒计时
            showAntiTimer();
            mTvAntiTimer.setText(String.valueOf(IVoiceMsgContract.MAX_DURATION - second));
        }
    }

}
