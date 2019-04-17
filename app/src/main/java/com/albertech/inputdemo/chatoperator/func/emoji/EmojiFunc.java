package com.albertech.inputdemo.chatoperator.func.emoji;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BasePagerAdapter;
import com.albertech.inputdemo.chatoperator.view.PagerDotIndicator;

import java.io.Serializable;


public class EmojiFunc extends EmojiFuncFragment implements OnEmojiClickListener {

    private final String EMOJI_LISTENER_KEY = "emoji_listener_key";


    private ViewPager mVpEmoji;
    private PagerDotIndicator mIndicator;


    private OnEmojiClickListener getListenerFromArgs() {
        OnEmojiClickListener listener = null;
        Bundle b = getArguments();
        if (b != null) {
            Serializable s = b.getSerializable(EMOJI_LISTENER_KEY);
            if (s instanceof OnEmojiClickListener) {
                listener = (OnEmojiClickListener) s;
            }
        }
        return listener;
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
        OnEmojiClickListener listener = getListenerFromArgs();

        BasePagerAdapter<View> adapter = new BasePagerAdapter<>();
        adapter.updatePagers(EmojiUtil.creatEmojiPagers(getContext()));
        mVpEmoji.setAdapter(adapter);
        mIndicator.setUpWithViewPager(mVpEmoji);
    }


    @Override
    public void onEmojiClick(String code) {

    }

    @Override
    public void onBackspaceClick() {

    }
}
