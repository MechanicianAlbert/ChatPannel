package com.albertech.easypannel.func.voice;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.albertech.easypannel.R;
import com.albertech.easypannel.util.PermissionUtil;

import java.io.File;


public class VoicePresenterImpl extends Handler implements IVoiceMsgContract.IVoicePresenter {

    /**
     * 录音按钮手势处理
     */
    private final BtnTouchTransformer BTN_WATCHER = new BtnTouchTransformer(this);

    /**
     * 录制监视, 作用:
     * 1. 录制超时停止
     * 2. 录制时长更新
     * 3. 录制音量更新
     */
    private final Runnable RECORD_WATCHER = new Runnable() {

        private final int WATCH_DURATION = 50;

        @Override
        public void run() {
            int currentSecond = (int) ((System.currentTimeMillis() - mStartRecordTime) / 1000);
            // 录制时长增长 1s 或以上
            if (currentSecond > mRecordDuration) {
                // 记录新的录制时长
                mRecordDuration = currentSecond;
                // 判断录制时长超时
                if (mRecordDuration > 60) {
                    // 停止录制, 自动发送录音文件
                    stopRecord(false);
                }
                // 更新录制时长到 UI
                POP.onTimeUpdate(currentSecond);
            }
            // 更新录制音量到 UI
            POP.onVolume(MODEL.getAmplitude());
            // 以 50ms 间隔轮询录制状态
            postDelayed(this, WATCH_DURATION);
        }
    };

    /**
     * 通话状态监听，用于录音过程中来电时, 放弃录音
     */
    private final PhoneStateListener PHONE_WATCHER = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // 如果通话状态改变为非闲置状态
            if (state != TelephonyManager.CALL_STATE_IDLE) {
                // 停止录音并丢弃录制文件
                stopRecord(true);
            }
        }
    };

    /**
     * 录音引擎
     */
    private final IVoiceMsgContract.IVoiceModel MODEL = new VoiceModelImpl();

    /**
     * 录音提示悬浮窗
     */
    private final VoiceViewPop POP;

    /**
     * 默认输出位置相对路径, 父路径为 SDCard
     */
    private final String RECORD_FILE_PARENT_RELATIVE_PATH;

    /**
     * 录音文件路径
     */
    private String mRecordPath;

    /**
     * 录音开始时间 (毫秒)
     */
    private long mStartRecordTime;

    /**
     * 录音持续时长 (秒)
     */
    private int mRecordDuration;

    private Context mContext;

    private View mBtn;

    private IVoiceMsgContract.IVoiceHandler mVoiceHandler;


    public VoicePresenterImpl(Context context, String path) {
        // 在构造中获得主线程
        super(Looper.getMainLooper());
        mContext = context;
        RECORD_FILE_PARENT_RELATIVE_PATH = path;
        POP = new VoiceViewPop(mContext);
        MODEL.init();
    }


    private boolean checkPermission(Activity activity) {
        int checkPermission = PermissionUtil.checkPermission(activity, PERMISSIONS);
        boolean permissionGranted;
        if (checkPermission == PermissionUtil.ALL_GRANTED) {
            permissionGranted = true;
        } else if (checkPermission >= PermissionUtil.DENIED_NEVER_ASK) {
            permissionGranted = false;
        } else {
            permissionGranted = false;
        }
        return permissionGranted;
    }

    private void startRecord() {
        long currentTime = System.currentTimeMillis();
        // 指定新录音的文件路径
        String name = "Rec_" + currentTime + ".amr";
        // 检查父路径合法性
        File parent = new File(Environment.getExternalStorageDirectory(), RECORD_FILE_PARENT_RELATIVE_PATH);
        if (parent.exists() || parent.mkdir()) {
            try {
                // 新建文件
                File file = new File(parent.getAbsolutePath(), name);
                // 设置录音文件可写
                file.setWritable(true);
                // 记录本次录音的文件全路径
                mRecordPath = file.getAbsolutePath();
                // 开启录制
                MODEL.startRecord(mRecordPath);
                // 记录录音开始时间;
                mStartRecordTime = currentTime;
                // 开启录制监视
                post(RECORD_WATCHER);
                // 通知浮窗录音开启
                POP.onRecordStart();
            } catch (IVoiceMsgContract.RecordInterceptByPermissionException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecord(boolean dropRecord) {
        // 停止录制
        MODEL.stopRecord();
        // 停止录制监视
        removeCallbacksAndMessages(null);
        // 通知浮窗录音结束
        POP.onRecordStop();
        if (dropRecord) {
            // 删除丢弃的录制文件
            if (mRecordPath != null) {
                File f = new File(mRecordPath);
                if (f != null && f.exists()) {
                    f.delete();
                }
            }
        } else {
            if (mRecordDuration > 1) {
                if (mVoiceHandler != null) {
                    mVoiceHandler.onVoiceSubmit(mRecordPath);
                }
            } else {
                Toast.makeText(mContext, R.string.str_toast_too_short, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void bindView(View btn) {
        mBtn = btn;
        if (mBtn != null) {
            mBtn.setOnTouchListener(BTN_WATCHER);
        }
        // 开始监听通话状态
//        ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE))
//                .listen(PHONE_WATCHER, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void releaseView() {
        if (mBtn != null) {
            mBtn.setOnTouchListener(null);
            mBtn = null;
        }
        MODEL.release();
        mVoiceHandler = null;
        // 停止监听通话状态
//        ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE))
//                .listen(PHONE_WATCHER, PhoneStateListener.LISTEN_NONE);

    }

    @Override
    public void setVoiceHandler(IVoiceMsgContract.IVoiceHandler voiceHandler) {
        mVoiceHandler = voiceHandler;
    }

    @Override
    public void onMaliciousContinualTouch() {
        // 可以提示不要频繁点击, 此处不做处理
    }

    @Override
    public void onFirstPress(MotionEvent event) {
        // 检查权限
        if (mContext instanceof Activity && checkPermission((Activity) mContext)) {
            // 开启录音
            startRecord();
        }
    }

    @Override
    public void onMovedIntoLimitArea(MotionEvent event) {
        // 通知浮窗触点在底部
        POP.onTouchPositiveArea();
        // 通知按钮触点在底部
        if (mBtn instanceof TextView) {
            ((TextView) mBtn).setText(R.string.str_hint_release_to_end);
        }
    }

    @Override
    public void onMovedOutOfLimitArea(MotionEvent event) {
        // 通知浮窗触点在上部
        POP.onTouchNegativeArea();
        // 通知按钮触点在上部
        if (mBtn instanceof TextView) {
            ((TextView) mBtn).setText(R.string.str_hint_release_to_cancel);
        }
    }

    @Override
    public void onReleasedFromLimitArea(MotionEvent event) {
        // 停止录音并丢弃录制内容
        stopRecord(false);
        // 重置按钮显示状态
        if (mBtn instanceof TextView) {
            ((TextView) mBtn).setText(R.string.str_hint_press_to_talk);
        }
    }

    @Override
    public void onReleasedOutOfLimitArea(MotionEvent event) {
        // 停止录音并发送录制内容
        stopRecord(true);
        // 重置按钮显示状态
        if (mBtn instanceof TextView) {
            ((TextView) mBtn).setText(R.string.str_hint_press_to_talk);
        }
    }

}
