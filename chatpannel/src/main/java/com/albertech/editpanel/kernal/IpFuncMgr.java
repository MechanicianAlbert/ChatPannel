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


/**
 * 输入面板管理类, 逻辑核心, 包内可见, 不可继承
 *
 * @author albert
 * 20181225
 */
final class IpFuncMgr extends Handler implements IDefaultFuncStatus, View.OnClickListener, View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

    /**
     * 输入面板状态切换监听
     */
    interface OnFuncStatusActivateListener {
        void onFuncActivate(int funcStatus);
    }


    /**
     * 扩展功能集合, Key: 扩展功能的激活入口按钮id, Value: 扩展功能
     */
    private final Map<Integer, IFunc> FUNCS = new ConcurrentHashMap<>();

    /**
     * 扩展功能入口按钮集合, Key: 扩展功能的激活入口按钮id, Value: 按钮View实例
     */
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


    void setFragmentManager(FragmentManager fm) {
        mFM = fm;
    }

    void setRootView(View rootView) {
        mRootView = rootView;
    }

    void setEditId(int editId) {
        mEditId = editId;
    }

    void setContainerId(int containerId) {
        mContainerId = containerId;
    }

    void setListener(IpFuncMgr.OnFuncStatusActivateListener listener) {
        mListener = listener;
    }

    void setDefaultContainerHeight(int height) {
        mContainerTargetHeight = height;
    }

    void setContainerHeightCoordinate(boolean coordinate) {
        mContainerHeightCoordinate = coordinate;
    }

    void setMaxContainerHeight(int height) {
        mMaxContainerHeight = height;
    }

    void setContainerReplaceMode(boolean replace) {
        mContainerReplaceMode = replace;
    }


    void init() {
        initView();
        initIMM();
        registerFunc(new HideFunc());
        registerFunc(new InputFunc());
    }

    void registerFunc(IFunc func) {
        if (func != null) {
            FUNCS.put(func.activatedStatus(), func);
            View trigger = mRootView.findViewById(func.triggerId());
            if (trigger != null) {
                TRIGGERS.put(func.activatedStatus(), trigger);
                trigger.setOnClickListener(this);
            }
        }
    }

    void onFuncRegisterComplete() {
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

    Map<Integer, IFunc> getRegisteredFuncs() {
        Map<Integer, IFunc> map = new ConcurrentHashMap<>(FUNCS);
        return map;
    }

    void hide() {
        dormantAllFuncsExceptTrigger(0);
        sendEmptyMessage(FUNC_HIDE);
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

    private void setFuncStatus(int status) {
        if (FUNCS.containsKey(status)) {
            // 根据新状态处理系统输入法
            prepareInput(status);
            // 根据新状态处理扩展功能区域容器
            prepareContainer(status);
            // 根据新状态处理扩展功能Fragment的填充或切换
            prepareContent(status);
            // 用新状态覆盖上一状态, 使之成为当前状态
            mCurrentStatus = status;
            // 通知状态变化监听器
            if (mListener != null) {
                mListener.onFuncActivate(mCurrentStatus);
            }
        }
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

    /**
     * 根据新状态处理系统输入法
     *
     * @param status 新状态
     */
    private void prepareInput(int status) {
        if (status == FUNC_INPUT) {
            // 只有输入框获得焦点后, 系统输入法管理才能借助其弹起输入法
            mEt.requestFocus();
            // 弹起系统输入法
            mIMM.showSoftInput(mEt, InputMethodManager.SHOW_IMPLICIT);
        } else {
            // 收起系统输入法
            mIMM.hideSoftInputFromWindow(mEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据新状态处理扩展功能区域容器
     *
     * @param status 新状态
     */
    private void prepareContainer(int status) {
        mContainer.setTargetHeight(FUNCS.get(status).needContainer() ? mContainerTargetHeight : 0);
    }

    /**
     * 根据新状态处理扩展功能Fragment的填充或切换
     *
     * @param status 新状态
     */
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
    public void onClick(View v) {
        dormantAllFuncsExceptTrigger(v.getId());
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        v.removeOnAttachStateChangeListener(this);
    }

    @Override
    public void onGlobalLayout() {
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
