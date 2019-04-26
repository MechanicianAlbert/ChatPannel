package com.albertech.easypannel.func.plus;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.albertech.common.base.pager.BasePagerAdapter;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.plus.api.IPlusConfig;
import com.albertech.easypannel.func.plus.api.OnPlusItemClickListener;
import com.albertech.easypannel.view.PagerDotIndicator;


public class PlusFunc extends PlusFuncFragment {

    private static final String CONFIG_KEY = "config";

    private final BasePagerAdapter<View> ADAPTER = new BasePagerAdapter<>();

    private OnPlusItemClickListener mListener;

    private ViewPager mVpPlus;
    private PagerDotIndicator mIndicator;


    public static PlusFunc newInstance(IPlusConfig config, OnPlusItemClickListener listener) {
        PlusFunc pf = new PlusFunc();
        Bundle b = new Bundle();
        b.putSerializable(CONFIG_KEY, config);
        pf.setArguments(b);
        pf.mListener = listener;
        return pf;
    }


    @Override
    protected void initArgs(Bundle args) {
        IPlusConfig config = (IPlusConfig) args.getSerializable(CONFIG_KEY);
        PlusManager.setPlusConfig(config);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_func_plus;
    }

    @Override
    protected void initView(View rootView) {
        mVpPlus = rootView.findViewById(R.id.vp_ip_func_plus);
        mIndicator = rootView.findViewById(R.id.pi_ip_func_plus_indicator);
    }

    @Override
    protected void initData() {
        ADAPTER.updatePagers(PlusManager.creatPlusPagers(getContext(), mListener));
        mVpPlus.setAdapter(ADAPTER);
        mIndicator.setUpWithViewPager(mVpPlus);
        mIndicator.setVisibility(ADAPTER.getCount() < 2 ? View.GONE : View.VISIBLE);
    }
}
