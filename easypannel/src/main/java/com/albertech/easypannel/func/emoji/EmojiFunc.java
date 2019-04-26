package com.albertech.easypannel.func.emoji;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.albertech.common.base.pager.BasePagerAdapter;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.emoji.api.IEmojiConfig;
import com.albertech.easypannel.func.emoji.api.OnEmojiClickListener;
import com.albertech.easypannel.view.PagerDotIndicator;


public class EmojiFunc extends EmojiFuncFragment {

    private static final String CONFIG_KEY = "config";

    private final BasePagerAdapter<View> ADAPTER = new BasePagerAdapter<>();

    private OnEmojiClickListener mListener;

    private ViewPager mVpEmoji;
    private PagerDotIndicator mIndicator;


    public static EmojiFunc newInstance(IEmojiConfig config, OnEmojiClickListener listener) {
        EmojiFunc ef = new EmojiFunc();
        Bundle b = new Bundle();
        b.putSerializable(CONFIG_KEY, config);
        ef.setArguments(b);
        ef.mListener = listener;
        return ef;
    }


    @Override
    protected void initArgs(Bundle args) {
        IEmojiConfig config = (IEmojiConfig) args.getSerializable(CONFIG_KEY);
        EmojiManager.setEmojiConfig(config);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_func_emoji;
    }

    @Override
    protected void initView(View rootView) {
        mVpEmoji = rootView.findViewById(R.id.vp_ip_func_emoji);
        mIndicator = rootView.findViewById(R.id.pi_ip_func_emoji_indicator);
    }

    @Override
    protected void initData() {
        ADAPTER.updatePagers(EmojiManager.creatEmojiPagers(getContext(), mListener));
        mVpEmoji.removeAllViews();
        mVpEmoji.setAdapter(ADAPTER);
        mIndicator.setUpWithViewPager(mVpEmoji);
    }

}
