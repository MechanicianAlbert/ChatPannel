package com.albertech.inputdemo.chatoperator.func.emoji;


import android.support.v4.view.ViewPager;
import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BasePagerAdapter;
import com.albertech.inputdemo.chatoperator.func.emoji.api.IEmojiConfig;
import com.albertech.inputdemo.chatoperator.view.PagerDotIndicator;


public class EmojiFunc extends EmojiFuncFragment {

    private final BasePagerAdapter<View> ADAPTER = new BasePagerAdapter<>();


    private EmojiManager mEmojiManager;

    private ViewPager mVpEmoji;
    private PagerDotIndicator mIndicator;


    public static EmojiFunc newInstance(IEmojiConfig config) {
        EmojiFunc ef = new EmojiFunc();
        ef.mEmojiManager = new EmojiManager(config);
        return ef;
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
        ADAPTER.updatePagers(mEmojiManager.creatEmojiPagers(getContext()));
        mVpEmoji.setAdapter(ADAPTER);
        mIndicator.setUpWithViewPager(mVpEmoji);
    }

}
