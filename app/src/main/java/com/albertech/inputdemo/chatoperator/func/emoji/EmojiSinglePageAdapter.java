package com.albertech.inputdemo.chatoperator.func.emoji;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BaseHolder;
import com.albertech.inputdemo.base.BaseRecyclerAdapter;


public class EmojiSinglePageAdapter extends BaseRecyclerAdapter<EmojiBean> {

    static class EmojiHolder extends BaseHolder<EmojiBean> {

        public EmojiHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(int position, EmojiBean emojiBean) {
            ImageView iv = $(R.id.iv_emoji);
            iv.setBackgroundResource(emojiBean.getRes());
        }
    }


    @Override
    protected int getLayoutResByViewType(int viewType) {
        return R.layout.item_emoji_icon;
    }

    @Override
    protected BaseHolder<EmojiBean> getHolderByViewType(int viewType, View itemView) {
        return new EmojiHolder(itemView);
    }

    @Override
    public void onItemClick(int position, EmojiBean emojiBean) {
        if (position == getItemCount() - 1) {
            Log.e("AAA", "Delete");
        } else {
            Log.e("AAA", "Res: " + emojiBean.getRes() + ", Code: " + emojiBean.getCode());
        }
    }
}
