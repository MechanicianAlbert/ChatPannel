package com.albertech.easypannel.func.stt;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.albertech.easypannel.R;

import java.lang.reflect.Constructor;

/**
 * @author Albert
 */
public class SttFunc extends SttFuncFragment {

    private static final int ON_DESTROY = -1;
    private static final int ON_INIT = 0;
    private static final int ON_VOLUME_CHANGED = 1;
    private static final int ON_BEGIN_OF_SPEECH = 2;
    private static final int ON_END_OF_SPEECH = 3;
    private static final int ON_RESULT = 4;
    private static final int ON_ERROR = 5;

    private static final int ORDER_INIT = 10;
    private static final int ORDER_DESTROY = 11;
    private static final int ORDER_START = 12;
    private static final int ORDER_STOP = 13;

    private Handler.Callback STT_CALLBACK = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ON_DESTROY:
                    break;
                case ON_INIT:
                    break;
                case ON_VOLUME_CHANGED:
                    onVolume(msg.arg1);
                    break;
                case ON_BEGIN_OF_SPEECH:
                    onSttBegin();
                    break;
                case ON_END_OF_SPEECH:
                    onSttEnd();
                    break;
                case ON_RESULT:
                    if (mListener != null) {
                        mListener.onSttResult(msg.obj.toString());
                    }
                    break;
                case ON_ERROR:
                    break;
            }
            return false;
        }
    };


    private OnSttResultListener mListener;

    private TextView mTv;
    private View mBtn;
    private Handler mSttHelper;


    public static SttFunc newInstance(OnSttResultListener listener) {
        SttFunc sf = new SttFunc();
        sf.mListener = listener;
        return sf;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            init();
        } else {
            destroy();
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_func_stt;
    }

    @Override
    protected void initView(View root) {
        mTv = root.findViewById(R.id.tv_stt);
        mBtn = root.findViewById(R.id.btn_stt);

        if (mTv != null) {
            mTv.setText(mTv.getContext().getResources().getString(R.string.str_stt_click_to_start));
        }
    }

    @Override
    protected void initListener() {
        if (mBtn != null) {
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isListening = v.isSelected();
                    if (isListening) {
                        stopListening();
                    } else {
                        startListening();
                    }
                }
            });
        }
    }

    @Override
    protected void initData() {
        try {
            Class c = Class.forName("com.albertech.iflysttlib.SttHelper");
            if (c != null) {
                Constructor constructor = c.getConstructor(Handler.Callback.class);
                mSttHelper = (Handler) constructor.newInstance(STT_CALLBACK);
                init();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSttBegin() {
        if (mTv != null) {
            mTv.setText(mTv.getContext().getResources().getString(R.string.str_stt_click_to_stop));
        }
        if (mBtn != null) {
            mBtn.setSelected(true);
        }
    }

    private void onSttEnd() {
        if (mTv != null) {
            mTv.setText(mTv.getContext().getResources().getString(R.string.str_stt_click_to_start));
        }
        if (mBtn != null) {
            mBtn.setSelected(false);
        }
    }

    private void onVolume(int volume) {
        // do nothing
    }

    private void init() {
        sendMessageByMethodAndParams(ORDER_INIT, 0, 0, getContext());
    }

    private void destroy() {
        sendMessageByMethodAndParams(ORDER_DESTROY, 0, 0, null);
    }

    private void startListening() {
        sendMessageByMethodAndParams(ORDER_START, 0, 0, null);
    }

    private void stopListening() {
        sendMessageByMethodAndParams(ORDER_STOP, 0, 0, null);
    }

    private void sendMessageByMethodAndParams(int what, int arg1, int arg2, Object obj) {
        if (mSttHelper != null) {
            Message.obtain(mSttHelper, what, arg1, arg2, obj).sendToTarget();
        }
    }

}
