package com.albertech.easypannel.func.emoji.adapter;


import android.view.View;

import com.albertech.common.base.recycler.normal.BaseRecyclerAdapter;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.emoji.bean.EmojiBean;


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
