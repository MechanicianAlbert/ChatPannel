package com.albertech.inputdemo.chatoperator.func.emoji.adapter;


import android.support.annotation.NonNull;
import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BaseHolder;
import com.albertech.inputdemo.base.BaseRecyclerAdapter;
import com.albertech.inputdemo.chatoperator.func.emoji.bean.EmojiBean;


public class EmojiHolder extends BaseHolder<BaseRecyclerAdapter<EmojiHolder, EmojiBean>, EmojiBean> {


    public EmojiHolder(BaseRecyclerAdapter<EmojiHolder, EmojiBean> adapter, @NonNull View itemView) {
        super(adapter, itemView);
    }

    @Override
    protected void onBind(int position, EmojiBean emojiBean) {
        setImage(R.id.iv_emoji, emojiBean.RES);
    }
}
