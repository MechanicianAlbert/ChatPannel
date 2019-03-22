package com.albertech.inputdemo.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    private View mRootView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(layoutRes(), container, false);
        initView(mRootView);
        initListener();
        initData();
        return mRootView;
    }


    protected View getRootView() {
        return mRootView;
    }

    protected void initArgs(Bundle bundle) {

    }

    protected void initView(View rootView) {

    }

    protected void initListener() {

    }

    protected void initData() {

    }


    protected abstract @LayoutRes int layoutRes();
}
