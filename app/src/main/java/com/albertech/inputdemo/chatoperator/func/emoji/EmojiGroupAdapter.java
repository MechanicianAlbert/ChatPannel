package com.albertech.inputdemo.chatoperator.func.emoji;


import android.util.Log;
import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BaseRecyclerAdapter;


public class EmojiGroupAdapter extends BaseRecyclerAdapter<EmojiHolder, EmojiBean> {


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_emoji_icon;
    }

    @Override
    protected EmojiHolder getHolderByViewType(View itemView, int viewType) {
        return new EmojiHolder(this, itemView);
    }

    @Override
    public boolean onItemClick(int position, EmojiBean emojiBean) {
        if (position == getItemCount() - 1) {
            Log.e("AAA", "Delete");
        } else {
            Log.e("AAA", "Res: " + emojiBean.getRes() + ", Code: " + emojiBean.getCode());
        }
        return false;
    }
}
