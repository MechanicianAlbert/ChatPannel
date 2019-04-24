package com.albertech.easypannel.func.plus;


import android.support.v4.view.ViewPager;
import android.view.View;

import com.albertech.easypannel.R;
import com.albertech.easypannel.base.BasePagerAdapter;
import com.albertech.easypannel.func.plus.api.IPlusConfig;
import com.albertech.easypannel.view.PagerDotIndicator;


public class PlusFunc extends PlusFuncFragment {

    private PlusManager mManager;
    private final BasePagerAdapter<View> ADAPTER = new BasePagerAdapter<>();


    private ViewPager mVpPlus;
    private PagerDotIndicator mIndicator;


    public static PlusFunc newInstance(IPlusConfig config) {
        PlusFunc pf = new PlusFunc();
        pf.mManager = new PlusManager(config);
        return pf;
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
        ADAPTER.updatePagers(mManager.creatPlusPagers(getContext()));
        mVpPlus.setAdapter(ADAPTER);
        mIndicator.setUpWithViewPager(mVpPlus);
        mIndicator.setVisibility(ADAPTER.getCount() < 2 ? View.GONE : View.VISIBLE);
    }
}
