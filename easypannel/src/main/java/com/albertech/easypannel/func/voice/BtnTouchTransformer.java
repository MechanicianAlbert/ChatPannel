package com.albertech.easypannel.func.voice;

import android.view.MotionEvent;
import android.view.View;

/**
 * 录音按钮手势监听
 */
public class BtnTouchTransformer implements View.OnTouchListener {

    /**
     * 触点位置状态监听接口
     */
    public interface Listener {

        /**
         * 过频繁点击
         */
        void onMaliciousContinualTouch();

        /**
         * 首次按下
         * @param event 触摸事件, 通常不必处理
         */
        void onFirstPress(MotionEvent event);

        /**
         * 滑动至限制区域内, 此时触点距底部较近, 松开后触发正事件 (如发送)
         * @param event 触摸事件, 通常不必处理
         */
        void onMovedIntoLimitArea(MotionEvent event);

        /**
         * 滑动至限制区域内, 此时触点距底部较近, 松开后触发负事件 (如取消)
         * @param event 触摸事件, 通常不必处理
         */
        void onMovedOutOfLimitArea(MotionEvent event);

        /**
         * 在限制区域内松开, 此时触点距底部较近, 触发正事件 (如发送)
         * @param event 触摸事件, 通常不必处理
         */
        void onReleasedFromLimitArea(MotionEvent event);

        /**
         * 在限制区域外松开, 此时触点距底部较近, 触发负事件 (如取消)
         * @param event 触摸事件, 通常不必处理
         */
        void onReleasedOutOfLimitArea(MotionEvent event);

    }


    /**
     * 上滑至限制区域外的最小距离
     */
    private static final int UPWARD_LIMIT_PX = 200;

    /**
     * 触发首次按下的最小时间间隔
     */
    private static final int MIN_INTERVAL_MILLIS = 1000;

    /**
     * 触摸位置状态的监听者
     */
    private Listener mListener;

    /**
     * 上次触发 首次按下 的时间 (毫秒)
     */
    private long mLastPressMillis;

    /**
     * 首次按下 时触点纵坐标
     */
    private float mStartY;

    /**
     * 当前触点纵坐标
     */
    private float mCurrentY;


    public BtnTouchTransformer(Listener listener) {
        mListener = listener;
    }


    private boolean belowLimit() {
        return mStartY - mCurrentY < UPWARD_LIMIT_PX;
    }

    private boolean checkFrequency(long currentTimeMillis) {
        return Math.abs(currentTimeMillis - mLastPressMillis) > MIN_INTERVAL_MILLIS;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCurrentY = event.getY();
        if (mListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 按下时
                    mStartY = event.getY();
                    long now = System.currentTimeMillis();
                    if (checkFrequency(now)) {
                        mLastPressMillis = now;
                        mListener.onFirstPress(event);
                    } else {
                        mListener.onMaliciousContinualTouch();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (belowLimit()) {
                        mListener.onReleasedFromLimitArea(event);
                    } else {
                        mListener.onReleasedOutOfLimitArea(event);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (belowLimit()) {
                        mListener.onMovedIntoLimitArea(event);
                    } else {
                        mListener.onMovedOutOfLimitArea(event);
                    }
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

}
