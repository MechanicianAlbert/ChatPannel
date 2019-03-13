package com.albertech.editpanel.kernal;


import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.view.View;


/**
 * 输入面板管理器建造者类, 包外可见, 不可继承
 *
 * @author albert
 * 20181225
 */
public final class IpMgrBuilder {

    // 根布局View
    private View mRootView;

    // Fragment管理器
    private FragmentManager mFM;

    // 输入框id
    private int mEditId;

    // 扩展功能区域容器id
    private int mContainerId;

    // 输入面板状态切换监听
    private IpFuncMgr.OnFuncStatusActivateListener mListener;

    // 默认容器高度
    private int mDefaultContainerHeight;

    // 容器高度与系统输入法高度联动
    private boolean mContainerHeightCoordinate;

    // 最大容器高度
    private int mMaxContainerHeight;

    // 扩展功能碎片从容器移除后, 是否销毁扩展功能碎片实例
    private boolean mContainerReplaceMode;


    public IpMgrBuilder fragmentManager(FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("FragmentManager cannot be null");
        }
        mFM = fm;
        return this;
    }

    public IpMgrBuilder rootView(View rootView) {
        if (rootView == null) {
            throw new NullPointerException("Root view cannot be null");
        }
        mRootView = rootView;
        return this;
    }

    public IpMgrBuilder editId(int editId) {
        mEditId = editId;
        return this;
    }

    public IpMgrBuilder containerId(int containerId) {
        mContainerId = containerId;
        return this;
    }


    public IpMgrBuilder setListener(IpFuncMgr.OnFuncStatusActivateListener listener) {
        mListener = listener;
        return this;
    }

    public IpMgrBuilder setDefaultContainerHeight(int height) {
        mDefaultContainerHeight = height;
        return this;
    }

    public IpMgrBuilder setContainerHeightCoordinate(boolean coordinate) {
        mContainerHeightCoordinate = coordinate;
        return this;
    }

    public IpMgrBuilder setMaxContainerHeight(int height) {
        mMaxContainerHeight = height;
        return this;
    }

    public IpMgrBuilder setContainerReplaceMode(boolean replace) {
        mContainerReplaceMode = replace;
        return this;
    }


    /**
     * 创建输入面板管理器实例
     * @return
     */
    IpFuncMgr build() {
        IpFuncMgr manager = new IpFuncMgr(Looper.getMainLooper());
        manager.setFragmentManager(mFM);
        manager.setRootView(mRootView);
        manager.setEditId(mEditId);
        manager.setContainerId(mContainerId);
        manager.setDefaultContainerHeight(mDefaultContainerHeight);
        manager.setContainerHeightCoordinate(mContainerHeightCoordinate);
        manager.setMaxContainerHeight(mMaxContainerHeight);
        manager.setContainerReplaceMode(mContainerReplaceMode);
        manager.setListener(mListener);
        manager.init();
        releaseSelf();
        return manager;
    }

    // 释放此建造者实例持有的外部引用, 保障其被正常GC
    private void releaseSelf() {
        mRootView = null;
        mFM = null;
        mListener = null;
    }
}
