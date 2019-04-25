package com.albertech.easypannel.func.emoji.adapter;


import android.support.annotation.NonNull;
import android.view.View;

import com.albertech.common.base.recycler.normal.BaseRecyclerAdapter;
import com.albertech.common.base.recycler.normal.BaseHolder;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.emoji.bean.EmojiBean;


public class EmojiHolder extends BaseHolder<BaseRecyclerAdapter<EmojiHolder, EmojiBean>, EmojiBean> {


    public EmojiHolder(BaseRecyclerAdapter<EmojiHolder, EmojiBean> adapter, @NonNull View itemView) {
        super(adapter, itemView);
    }

    @Override
    protected void onBind(int position, EmojiBean emojiBean) {
        setImage(R.id.iv_emoji, emojiBean.RES);
    }
}
