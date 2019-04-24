package com.albertech.inputdemo.chatoperator.func.plus;


import android.support.v4.view.ViewPager;
import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BasePagerAdapter;
import com.albertech.inputdemo.chatoperator.func.plus.api.IPlusConfig;
import com.albertech.inputdemo.chatoperator.func.plus.api.OnPlusItemClickListener;
import com.albertech.inputdemo.chatoperator.view.PagerDotIndicator;


public class PlusFunc extends PlusFuncFragment {

    private PlusManager mManager;
    private final BasePagerAdapter<View> ADAPTER = new BasePagerAdapter<>();


//    private OnPlusItemClickListener mListener;

    private ViewPager mVpPlus;
    private PagerDotIndicator mIndicator;


    public static PlusFunc newInstance(OnPlusItemClickListener listener) {
        PlusFunc pf = new PlusFunc();
//        pf.mListener = listener;
        return pf;
    }

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
