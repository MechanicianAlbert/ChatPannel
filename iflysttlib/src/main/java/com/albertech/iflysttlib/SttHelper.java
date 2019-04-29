package com.albertech.iflysttlib;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * @author Albert
 */
public class SttHelper extends Handler implements RecognizerListener, InitListener {

    private static final int ON_DESTROY = -1;
    private static final int ON_INIT = 0;
    private static final int ON_VOLUME_CHANGED = 1;
    private static final int ON_BEGIN_OF_SPEECH = 2;
    private static final int ON_END_OF_SPEECH = 3;
    private static final int ON_RESULT = 4;
    private static final int ON_ERROR = 5;
    private static final int ON_EVENT = 6;

    private static final int ORDER_INIT = 10;
    private static final int ORDER_DESTROY = 11;
    private static final int ORDER_START = 12;
    private static final int ORDER_STOP = 13;


    private static final String APP_ID = "";


    private SpeechRecognizer mRecognizer;


    public SttHelper(Handler.Callback callback) {
        super(Looper.getMainLooper(), callback);
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ORDER_INIT:
                Context context = (Context) msg.obj;
                if (context != null) {
                    init(context, APP_ID);
                }
                break;
            case ORDER_DESTROY:
                destroy();
                break;
            case ORDER_START:
                startListening();
                break;
            case ORDER_STOP:
                stopListening();
                break;
        }
    }


    // ISttPresenter
    public void init(Context context, String appId) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + appId);
        mRecognizer = SpeechRecognizer.createRecognizer(context, this);
        if (mRecognizer == null) {
            sendMessageByMethodAndParams(ON_DESTROY, 0, 0, null);
            return;
        }
        mRecognizer.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mRecognizer.setParameter(SpeechConstant.SUBJECT, null);
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "plain");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mRecognizer.setParameter(SpeechConstant.VAD_BOS, "2000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mRecognizer.setParameter(SpeechConstant.VAD_EOS, "2000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");
    }

    public void destroy() {
        sendMessageByMethodAndParams(ON_DESTROY, 0, 0, null);
        removeCallbacksAndMessages(null);
        if (mRecognizer != null) {
            mRecognizer.destroy();
        }
    }

    public void cancel() {
        if (mRecognizer != null) {
            mRecognizer.cancel();
        }
    }

    public boolean isListening() {
        return mRecognizer != null && mRecognizer.isListening();
    }

    public void startListening() {
        if (mRecognizer != null) {
            mRecognizer.startListening(this);
        }
    }

    public void stopListening() {
        sendMessageByMethodAndParams(ON_END_OF_SPEECH, 0, 0, null);
        if (mRecognizer != null && mRecognizer.isListening()) {
            mRecognizer.stopListening();
        }
    }

    // InitListener
    @Override
    public void onInit(int errorCode) {
        sendMessageByMethodAndParams(ON_INIT, errorCode, 0, null);
    }

    // RecognizerListener
    @Override
    public void onVolumeChanged(int volume, byte[] data) {
        sendMessageByMethodAndParams(ON_VOLUME_CHANGED, volume, 0, null);
    }

    @Override
    public void onBeginOfSpeech() {
        sendMessageByMethodAndParams(ON_BEGIN_OF_SPEECH, 0, 0, null);
    }

    @Override
    public void onEndOfSpeech() {
        sendMessageByMethodAndParams(ON_END_OF_SPEECH, 0, 0, null);
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean islast) {
        sendMessageByMethodAndParams(ON_RESULT, islast ? 0 : 1, 0, recognizerResult.getResultString());
    }

    @Override
    public void onError(SpeechError speechError) {
        sendMessageByMethodAndParams(ON_ERROR, speechError.getErrorCode(), 0, null);
    }

    @Override
    public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        sendMessageByMethodAndParams(ON_EVENT, arg1, arg2, obj);
    }


    private void sendMessageByMethodAndParams(int what, int arg1, int arg2, Object obj) {
        Message.obtain(this, what, arg1, arg2, obj).sendToTarget();
    }

}
