package com.albertech.inputdemo.chatoperator.func.plus;


import android.support.v4.view.ViewPager;
import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BasePagerAdapter;
import com.albertech.inputdemo.chatoperator.view.PagerDotIndicator;


public class PlusFunc extends PlusFuncFragment {

    private ViewPager mVpPlus;
    private PagerDotIndicator mIndicator;


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
        BasePagerAdapter<View> adapter = new BasePagerAdapter<>();
        adapter.updatePagers(PlusUtil.createPlusPagers(getContext()));
        mVpPlus.setAdapter(adapter);
        mIndicator.setUpWithViewPager(mVpPlus);

        mIndicator.setVisibility(adapter.getCount() < 2 ? View.GONE : View.VISIBLE);
    }
}
