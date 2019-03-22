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
final class IpFuncMgrImpl extends Handler implements IpFuncMgr, IDefaultFuncStatus, View.OnClickListener, View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

    /**
     * 输入面板状态切换监听
     */
    interface OnFuncStatusActivateListener {
        void onFuncActivate(int funcStatus);
    }


    /**
     * 扩展功能集合, Key: 扩展功能被激活时对应的输入状态, Value: 扩展功能
     */
    private final Map<Integer, IFunc> FUNCS = new ConcurrentHashMap<>();

    /**
     * 扩展功能入口按钮集合, Key: 扩展功能被激活时对应的输入状态, Value: 扩展功能入口按钮View实例
     */
    private final Map<Integer, View> TRIGGERS = new ConcurrentHashMap<>();


    // Fragment管理器
    private FragmentManager mFM;

    // 输入面板根布局View
    private View mRootView;

    // 输入编辑框id
    private int mEditId;

    // 扩展功能区域容器id
    private int mContainerId;

    // 输入面板状态切换监听器
    private OnFuncStatusActivateListener mListener;

    // 扩展功能容器目标高度
    private int mContainerTargetHeight = 600;

    // 扩展功能容器最大高度
    private int mMaxContainerHeight = Integer.MAX_VALUE;

    // 扩展功能容器高度与系统输入法面板弹出后高度的绑定,
    // 设位 true 后开启绑定, 当输入法改变高度, 容器目标高度会随之变换
    private boolean mContainerHeightCoordinate;

    // 扩展功能碎片从容器移除后, 是否销毁扩展功能碎片实例
    private boolean mContainerReplaceMode;


    // 输入编辑框实例, 需要操作此实例可见并获取焦点, 才可以主动弹出系统输入法
    private EditText mEt;

    // 扩展功能容器实例, 调整目标高度时操作
    private IpFuncContainer mContainer;

    // 系统输入法管理器
    private InputMethodManager mIMM;

    // 最近一次系统输入法高度发生变化时记录的高度
    private int mLastInputMethodHeight;

    // 输入面板的当前状态, 可由IDefaultFuncStatus接口或其子类查询预定义的输入状态
    private int mCurrentStatus = FUNC_HIDE;


    // 构造传入主线程的Looper实例, 可使输入状态切换的逻辑流程在主线程执行
    IpFuncMgrImpl(Looper looper) {
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

    void setListener(IpFuncMgrImpl.OnFuncStatusActivateListener listener) {
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


    /**
     * 输入状态管理器的总初始化
     */
    void init() {
        // 管理器绑定输入面板根布局View
        initView();
        // 初始化系统输入法管理器
        initIMM();
        // 注册默认输入状态
        initDefaultFunc();
    }




    /**
     * 管理器绑定输入面板根布局View
     */
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

    /**
     * 初始化系统输入法管理器
     */
    private void initIMM() {
        mIMM = (InputMethodManager) mRootView.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 注册默认输入状态
     */
    private void initDefaultFunc() {
        // 隐藏功能区
        registerFunc(new HideFunc());
        // 系统输入法
        registerFunc(new InputFunc());
    }

    /**
     * 切换输入状态方法
     * @param status 新状态
     */
    private void updateFuncStatus(int status) {
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

    /**
     * 将激活功能的入口置为激活外观, 非激活功能的入口置为非激活的外观
     * @param triggerId
     */
    private void dormantAllFuncsExceptTrigger(int triggerId) {
        // 遍历缓存的扩展功能
        for (int status : FUNCS.keySet()) {
            IFunc func = FUNCS.get(status);
            if (func != null) {
                View trigger = TRIGGERS.get(status);
                if (trigger != null) {
                    // 扩展功能的入口view实例id与当前激活入口view实例id相同
                    if (triggerId == func.triggerId()) {
                        // 激活入口view
                        boolean hasBeenActivated = mCurrentStatus == status;
                        trigger.setBackgroundResource(hasBeenActivated ? func.activatedRes() : func.dormantRes());
                        sendEmptyMessage(hasBeenActivated ? func.dormantStatus() : func.activatedStatus());
                    } else {
                        // 没有激活的入口view, 设为挂起
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


    /**
     * 向管理器注册扩展功能
     * @param func 扩展功能
     */
    @Override
    public void registerFunc(IFunc func) {
        if (func != null) {
            // 缓存扩展功能实例, Key: 激活功能时的输入状态, Value: 扩展功能实例
            FUNCS.put(func.activatedStatus(), func);
            // 查找扩展功能入口按钮View, 对于没有入口的功能, triggerId()返回0, 查不到任何view, 需要判空
            View trigger = mRootView.findViewById(func.triggerId());
            // trigger非空表明扩展功能有入口
            if (trigger != null) {
                // 缓存此入口按钮View实例
                TRIGGERS.put(func.activatedStatus(), trigger);
                // 注册入口按钮的点击监听, 统一处理激活扩展功能的逻辑
                trigger.setOnClickListener(this);
            }
        }
    }

    /**
     * 所有扩展功能注册完成, 此方法可以将Fragment预加载
     */
    @Override
    public void onFuncRegisterComplete() {
        // 若功能切换模式为不销毁, 需要预先将缓存好的扩展功能布局Fragment全部添加到Fragment管理器中
        if (!mContainerReplaceMode) {
            // 将缓存的所有扩展功能添加到Fragment管理器
            FragmentTransaction transaction = mFM.beginTransaction();
            for (int status : FUNCS.keySet()) {
                Fragment fragment = FUNCS.get(status).content();
                transaction.add(mContainerId, fragment);
                // 默认输入状态为隐藏,
                if (status != FUNC_HIDE) {
                    // 激活隐藏状态, 显示隐藏状态对应的功能布局, 即空Fragment, 不展示任何功能布局
                    transaction.hide(fragment);
                } else {
                    // 将其他扩展功能布局Fragment全部隐藏
                    transaction.show(fragment);
                }
            }
            // 注意允许StateLoss, 保证再未切换完成时退出输入面板根布局所在的Activity不会出现异常;
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 获取扩展功能注册表, 要返回一个注册表副本, 以免注册表被其他生命周期更长的实例长期持有导致内存泄漏
     *
     * @return 扩展功能注册表副本
     */
    @Override
    public Map<Integer, IFunc> getRegisteredFuncs() {
        return new ConcurrentHashMap<>(FUNCS);
    }

    /**
     * 主动切换输入状态
     */
    @Override
    public void setFuncStatus(int status) {
        sendEmptyMessage(status);
    }

    /**
     * 收起输入面板, 即状态切换为隐藏
     */
    @Override
    public void hide() {
        dormantAllFuncsExceptTrigger(0);
        sendEmptyMessage(FUNC_HIDE);
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
        // 在根布局被detach的时候, 移除ViewTree监听, 防止内存泄漏
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
        updateFuncStatus(msg.what);
    }
}
