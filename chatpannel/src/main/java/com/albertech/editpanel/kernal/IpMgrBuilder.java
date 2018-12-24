package com.albertech.editpanel.kernal;


import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.view.View;


public final class IpMgrBuilder {

    private View mRootView;
    private FragmentManager mFM;
    private int mEditId;
    private int mContainerId;

    private IpFuncMgr.OnFuncStatusActivateListener mListener;

    private int mDefaultContainerHeight;
    private boolean mContainerHeightCoordinate;
    private int mMaxContainerHeight;
    private boolean mContainerReplaceMode;


    public final IpMgrBuilder fragmentManager(FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException();
        }
        mFM = fm;
        return this;
    }

    public final IpMgrBuilder rootView(View rootView) {
        if (rootView == null) {
            throw new NullPointerException("");
        }
        mRootView = rootView;
        return this;
    }

    public final IpMgrBuilder editId(int editId) {
        mEditId = editId;
        return this;
    }

    public final IpMgrBuilder containerId(int containerId) {
        mContainerId = containerId;
        return this;
    }


    public final IpMgrBuilder setListener(IpFuncMgr.OnFuncStatusActivateListener listener) {
        mListener = listener;
        return this;
    }

    public final IpMgrBuilder setDefaultContainerHeight(int height) {
        mDefaultContainerHeight = height;
        return this;
    }

    public final IpMgrBuilder setContainerHeightCoordinate(boolean coordinate) {
        mContainerHeightCoordinate = coordinate;
        return this;
    }

    public final IpMgrBuilder setMaxContainerHeight(int height) {
        mMaxContainerHeight = height;
        return this;
    }

    public final IpMgrBuilder setContainerReplaceMode(boolean replace) {
        mContainerReplaceMode = replace;
        return this;
    }


    final IpFuncMgr build() {
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

    private void releaseSelf() {
        mRootView = null;
        mFM = null;
        mListener = null;
    }
}
