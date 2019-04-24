package com.albertech.inputdemo.chatoperator.func.emoji.adapter;


import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BaseRecyclerAdapter;
import com.albertech.inputdemo.chatoperator.func.emoji.bean.EmojiBean;


public class EmojiGroupAdapter extends BaseRecyclerAdapter<EmojiHolder, EmojiBean> {


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_emoji_icon;
    }

    @Override
    protected EmojiHolder getHolderByViewType(View itemView, int viewType) {
        return new EmojiHolder(this, itemView);
    }

}
