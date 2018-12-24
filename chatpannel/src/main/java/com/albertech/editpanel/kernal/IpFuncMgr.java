package com.albertech.editpanel.kernal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.albertech.editpanel.base.IDefaultFuncStatus;
import com.albertech.editpanel.base.IFunc;
import com.albertech.editpanel.defunc.HideFunc;
import com.albertech.editpanel.defunc.InputFunc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


final class IpFuncMgr extends Handler implements IDefaultFuncStatus, View.OnClickListener, View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

    interface OnFuncStatusActivateListener {
        void onFuncActivate(int funcStatus);
    }


    private final Map<Integer, IFunc> FUNCS = new ConcurrentHashMap<>();
    private final Map<Integer, View> TRIGGERS = new ConcurrentHashMap<>();


    private FragmentManager mFM;
    private View mRootView;
    private int mEditId;
    private int mContainerId;

    private OnFuncStatusActivateListener mListener;

    private int mContainerTargetHeight = 600;
    private int mMaxContainerHeight = Integer.MAX_VALUE;
    private boolean mContainerHeightCoordinate;
    private boolean mContainerReplaceMode;


    private EditText mEt;
    private IpFuncContainer mContainer;
    private InputMethodManager mIMM;
    private int mLastInputMethodHeight;
    private int mCurrentStatus;


    IpFuncMgr(Looper looper) {
        super(looper);
    }

    final void setFragmentManager(FragmentManager fm) {
        mFM = fm;
    }

    final void setRootView(View rootView) {
        mRootView = rootView;
    }

    final void setEditId(int editId) {
        mEditId = editId;
    }

    final void setContainerId(int containerId) {
        mContainerId = containerId;
    }

    final void setListener(IpFuncMgr.OnFuncStatusActivateListener listener) {
        mListener = listener;
    }

    final void setDefaultContainerHeight(int height) {
        mContainerTargetHeight = height;
    }

    final void setContainerHeightCoordinate(boolean coordinate) {
        mContainerHeightCoordinate = coordinate;
    }

    final void setMaxContainerHeight(int height) {
        mMaxContainerHeight = height;
    }

    final void setContainerReplaceMode(boolean replace) {
        mContainerReplaceMode = replace;
    }

    final void registerFunc(IFunc func) {
        if (func != null) {
            FUNCS.put(func.activatedStatus(), func);
            View trigger = mRootView.findViewById(func.triggerId());
            if (trigger != null) {
                TRIGGERS.put(func.activatedStatus(), trigger);
                trigger.setOnClickListener(this);
            }
        }
    }

    final void hide() {
        dormantAllFuncsExceptTrigger(0);
        sendEmptyMessage(FUNC_HIDE);
    }

    final void onFuncRegisterComplete() {
        if (!mContainerReplaceMode) {
            FragmentTransaction transaction = mFM.beginTransaction();
            for (int status : FUNCS.keySet()) {
                Fragment fragment = FUNCS.get(status).content();
                transaction.add(mContainerId, fragment);
                if (status != FUNC_HIDE) {
                    transaction.hide(fragment);
                } else {
                    transaction.show(fragment);
                }
            }
            transaction.commitAllowingStateLoss();
        }
    }

    final Map<Integer, IFunc> getRegisteredFuncs() {
        Map<Integer, IFunc> map = new ConcurrentHashMap<>(FUNCS);
        return map;
    }

    final void setFuncStatus(int status) {
        if (FUNCS.containsKey(status)) {
            prepareInput(status);
            prepareContainer(status);
            prepareContent(status);
            mCurrentStatus = status;
            if (mListener != null) {
                mListener.onFuncActivate(mCurrentStatus);
            }
        }
    }


    final void init() {
        initView();
        initIMM();
        registerFunc(new HideFunc());
        registerFunc(new InputFunc());
    }

    private void initView() {
        mEt = mRootView.findViewById(mEditId);
        if (mEt == null) {
            throw new RuntimeException("Id of EditText is error");
        }
        mContainer = mRootView.findViewById(mContainerId);
        if (mContainer == null) {
            throw new RuntimeException("Id of container view is error");
        }
        mRootView.addOnAttachStateChangeListener(this);
    }

    private void initIMM() {
        mIMM = (InputMethodManager) mRootView.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void dormantAllFuncsExceptTrigger(int triggerId) {
        for (int status : FUNCS.keySet()) {
            IFunc func = FUNCS.get(status);
            if (func != null) {
                View trigger = TRIGGERS.get(status);
                if (trigger != null) {
                    if (triggerId == func.triggerId()) {
                        boolean hasBeenActivated = mCurrentStatus == status;
                        trigger.setBackgroundResource(hasBeenActivated ? func.activatedRes() : func.dormantRes());
                        sendEmptyMessage(hasBeenActivated ? func.dormantStatus() : func.activatedStatus());
                    } else {
                        trigger.setBackgroundResource(func.activatedRes());
                    }
                }
            }
        }
    }

    private void prepareInput(int status) {
        if (status == FUNC_INPUT) {
            mIMM.showSoftInput(mEt, InputMethodManager.SHOW_IMPLICIT);
        } else {
            mIMM.hideSoftInputFromWindow(mEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void prepareContainer(int status) {
        mContainer.setTargetHeight(FUNCS.get(status).needContainer() ? mContainerTargetHeight : 0);
    }

    private void prepareContent(int status) {
        Fragment target = FUNCS.get(status).content();
        FragmentTransaction transaction = mFM.beginTransaction();
        if (!mContainerReplaceMode) {
            for (Fragment fragment : mFM.getFragments()) {
                if (target == fragment) {
                    transaction.show(fragment);
                } else {
                    transaction.hide(fragment);
                }
            }
        } else {
            transaction.replace(mContainerId, target);
        }
        transaction.commitAllowingStateLoss();
    }


    @Override
    public final void onClick(View v) {
        dormantAllFuncsExceptTrigger(v.getId());
    }

    @Override
    public final void onViewAttachedToWindow(View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public final void onViewDetachedFromWindow(View v) {
        v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        v.removeOnAttachStateChangeListener(this);
    }

    @Override
    public final void onGlobalLayout() {
        int currentInputMethodHeight = IpUtil.getInputMethodHeight(mRootView);
        if (mLastInputMethodHeight != currentInputMethodHeight) {
            mLastInputMethodHeight = currentInputMethodHeight;
            if (mLastInputMethodHeight > 0) {
                if (mContainerHeightCoordinate) {
                    mContainerTargetHeight = Math.min(mLastInputMethodHeight, mMaxContainerHeight);
                }
                if (mCurrentStatus != FUNC_INPUT) {
                    dormantAllFuncsExceptTrigger(0);
                    sendEmptyMessage(FUNC_INPUT);
                }
            } else {
                if (mCurrentStatus == FUNC_INPUT) {
                    hide();
                }
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        setFuncStatus(msg.what);
    }
}
